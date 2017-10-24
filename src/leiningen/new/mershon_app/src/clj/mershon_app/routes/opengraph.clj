(ns {{ns-name}}.routes.opengraph
  (:use [ring.util.response])
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [compojure.core :refer :all]

            [hbs.core :as handlebars]

            [{{ns-name}}.schema :as sql]))

(defonce base-config
  {:application_name        "{{ns-name}}"
   :application_description ""
   :application_url         ""
   :application_keywords    ""
   :application_share_logo  ""})

(defn with-configuration
  ([filename]
   (with-configuration filename nil))
  ([filename overrides]
   (let [template    (slurp (io/resource filename))
         config      (if (not-empty overrides)
                       (merge base-config overrides)
                       base-config)]
     (handlebars/render template config))))

(defn render-index
  [{:keys [application_name
           application_description
           application_url
           application_share_logo] :as config}]
  {:headers {"content-type" "text/html; charset=utf-8"}
   :body    (with-configuration "private/index.html" config)
   :status  200})

(def opengraph-index-routes
  (GET "*" _ (render-index {}))
  ; FIXME -- uncomment and implement to use
  ;(GET "/example-path/:example-uid" [example-uid]
  ;     (let [example-data (sql/get-exampledata-by-uid {:uid example-uid} {:result-set-fn first})]
  ;       (render-index {:example_handlebars_attribute "example handlebars attribute value"})))
  )
