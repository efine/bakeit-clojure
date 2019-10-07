(ns bakeit.upload
  (:require [clj-http.client :as http])
  (:require [clojure.data.json :as json]))

(defn make-query-params [api-key title language duration max-views]
  {:api_key   api-key
   :title     title
   :language  language
   :duration  duration
   :max_views max-views
   })

(defn upload [url query-params request-data]
  "Uploads `request-data` to `url` using `query-params` for options.
  Evaluates to either {:url url} on successful upload, or
  {:error status-code :response http-response-map} on error."
  (let [params          {:content-type :application/octet-stream
                         :query-params query-params
                         :body         request-data}
        response        (http/post url params)
        status          (:status response)
        from-json       (fn [body] (json/read-str body :key-fn keyword))
        print-paste-url (fn [url] (do (printf "Paste URL: `%s`" url) {:url url}))]
    (cond
      (some #{status} [200 201])
      (-> response :body from-json :url print-paste-url)
      (> status 299)
      {:error status :response response})))
