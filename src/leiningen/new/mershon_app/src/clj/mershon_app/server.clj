(ns {{ns-name}}.server
  (:require [clojure.java.io :as io]
            [environ.core :refer [env]]

            ; ring
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.gzip :refer [wrap-gzip]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.logger :refer [wrap-with-logger]]
            [ring.adapter.jetty :refer [run-jetty]]

            ; compojure
            [compojure.core :refer [ANY GET PUT POST DELETE OPTIONS context defroutes]]
            [compojure.route :refer [resources]]

            [{{ns-name}}.schema :as schema])
  (:gen-class))

(defroutes routes
  (resources "/")

  (context "/api" []
    (-> (defroutes api-routes
          ; Uncomment to enable CORS
          ;(OPTIONS "*" {headers :headers}
          ;         {:headers {"Access-Control-Allow-Origin" "*"
          ;                    "Access-Control-Allow-Methods" "GET, POST, PUT, DELETE"
          ;                    "Access-Control-Allow-Headers" (get headers "access-control-request-headers")}})

          ; add child contexts here
)
        wrap-json-response
        wrap-keyword-params
        wrap-json-params))

  ; any route not predefined should kick to the index page
  (GET "*" _
       {:status 200
        :headers {"Content-Type" "text/html; charset=utf-8"}
        :body (io/input-stream (io/resource "public/index.html"))}))

(def http-handler
  (-> routes
      (wrap-defaults api-defaults)
      wrap-with-logger
      wrap-gzip))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 10555))]
    (schema/do-update!)

    (run-jetty http-handler {:port port :join? false})))
