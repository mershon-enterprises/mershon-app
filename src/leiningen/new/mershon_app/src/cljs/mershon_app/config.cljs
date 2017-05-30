(ns {{ns-name}}.config)

(defonce dev-config     {:host  "localhost"
                         :port  3449
                         :proto "http"})
(defonce staging-config {:host  "TBD"
                         :port  80
                         :proto "http"})
(defonce prod-config    {:host  "TBD"
                         :port  443
                         :proto "https"})

(def config dev-config)

(defn app-url
  []
  (str (:proto config) "://" (:host config) ":" (:port config)))
