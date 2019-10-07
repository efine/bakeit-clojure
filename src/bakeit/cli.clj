(ns bakeit.cli
  (:require [bakeit.ini :as ini])
  (:require [clojure.tools.cli :as cli]))

(def version "0.1.0")
(def prog-name "bakeit")
(defn show-version [] (printf "Version %s\n" version))

(def arg-specs
  [["-h" "--help" "Print this help" :default false]
   ["-k" "--show-key" "Print api key" :default false]
   ["-V" "--version" "Print version" :default false]
   ["-t" "--title TITLE" "Paste title" :default ""]
   ["-l" "--lang LANG" "Language highlighter" :default ""]
   ["-d" "--duration DUR" "Paste expiry minutes" :default 60]
   ["-v" "--max-views MV" "Max # of views" :default 0]
   ["-b" "--open-browser" "Auto-open browser" :default false]])

(defn usage [summary]
  (println (str "Usage: " prog-name " [options] file-name"))
  (println summary))

(defn handle-errors [errors summary]
  (println "Invalid command line")
  (map #(println (str "- " %)) errors)
  (usage summary)
  )

(defn parse-args [args handle-upload]
  (let [{:keys [options arguments summary errors]} (cli/parse-opts args arg-specs)]
    (cond
      (not-empty errors) (handle-errors errors summary)
      (or (:help options)
          (empty? arguments)) (usage summary)
      (:version options) (show-version)
      (:show-key options) (println (ini/api-key))
      (and (not-empty options) (= (count arguments) 1)) (handle-upload options arguments))))




