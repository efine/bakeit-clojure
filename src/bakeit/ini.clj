(ns bakeit.ini
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as s]))

"This is all taken from https://github.com/jonase/clojure-ini/blob/master/src/clojure_ini/core.clj"

(defn- parse-line [s kw trim]
  (if (= (first s) \[)
    (-> s (subs 1 (.indexOf s "]")) trim kw)
    (let [n (.indexOf s "=")]
      (if (neg? n)
        (throw (Exception. (str "Could not parse: " s)))
        [(-> s (subs 0 n) trim kw)
         (-> s (subs (inc n)) trim)]))))

(defn- strip-comment [s chr allow-anywhere?]
  (let [n (.indexOf s (int chr))]
    (if (and (not (neg? n))
             (or allow-anywhere?
                 (zero? n)))
      (subs s 0 n)
      s)))

(defn- mapify [coll]
  (loop [xs coll m {} key nil]
    (if-let [x (first xs)]
      (if (vector? x)
        (if (nil? key)
          (recur (rest xs)
                 (assoc m (first x) (second x))
                 key)
          (recur (rest xs)
                 (assoc-in m [key (first x)] (second x))
                 key))
        (recur (rest xs)
               (assoc m x {})
               x))
      m)))

(defn read-ini
  "Read an .ini-file into a Clojure map.

  Valid options are:

  - keywordize? (default false): Turn segments and property-keys into
    keywords
  - trim? (default true): trim segments, keys and values
  - allow-comments-anywhere? (default true): Comments can appear
    anywhere, and not only at the beginning of a line
  - comment-char (default \\;)"
  [in & {:keys [keywordize?
                trim?
                allow-comments-anywhere?
                comment-char]
         :or {keywordize? false
              trim? true
              allow-comments-anywhere? true
              comment-char \;}}]
  {:pre [(char? comment-char)]}
  (let [kw (if keywordize? keyword identity)
        trim (if trim? s/trim identity)]
    (with-open [r (io/reader in)]
      (->> (line-seq r)
           (map #(strip-comment % comment-char allow-comments-anywhere?))
           (remove (fn [s] (every? #(Character/isWhitespace ^char %) s)))
           (map #(parse-line % kw trim))
           mapify))))

(def home-directory (System/getProperty "user.home"))
(def ini-file (io/file home-directory ".config/bakeit.cfg"))

(defn api-key []
  "Return the Pastery API key in a string"
  (let [ini-data (read-ini ini-file :keywordize? true)]
  (-> ini-data :pastery :api_key)))
