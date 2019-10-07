(ns bakeit.core
  (:require [bakeit.cli :as cli]
            [bakeit.upload :as upl]
            [bakeit.ini :as ini]))

(def pastery-paste-api "https://www.pastery.net/api/paste/")

(defn title-or-filename [options, file-name]
  (let [title (:title options)]
    (if (empty? title)
      file-name
      title)))

(defn handle-upload [options arguments]
  ""
  (let [file-name (first arguments)
        data      (slurp file-name)
        api-key   (ini/api-key)
        title     (title-or-filename options file-name)
        language  (:language options)
        duration  (:duration options)
        max-views (:max-views options)
        query-params (upl/make-query-params api-key title language duration max-views)]
    (assoc (upl/upload pastery-paste-api query-params data) :options options)))

(defn maybe-open-browser [options url]
  (if (:open-browser options)
    (clojure.java.browse/browse-url url)))

(defn -main [& args]
  (let [response (cli/parse-args args handle-upload)]
    (if-let [error (:error response)]
      (println (str "Error: " error))
      (maybe-open-browser (:options response) (:url response)))))