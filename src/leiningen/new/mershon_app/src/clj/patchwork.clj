(ns patchwork
  (:require [environ.core :refer [env]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]))

(defonce config (atom {}))

(defn init
  [{:keys [base-url account-id api-key] :as args}]
  (reset! config args))
(init {:base-url   (:patchwork-base-url   env)
       :account-id (:patchwork-account-id env)
       :api-key    (:patchwork-api-key    env)})

(defn call-platform-method
  [{:keys [platform-id http-action platform-method params] :as args}]

  (let [{base-url   :base-url
         account-id :account-id
         api-key    :api-key} @config
        callable              (case (.toLowerCase http-action)
                                "post"   client/post
                                "get"    client/get
                                "put"    client/put
                                "delete" client/delete)]
    (let [response (callable
                    (str base-url      "/api"
                         "/account/"   account-id
                         "/platforms/" platform-id
                         "/"           platform-method)
                    (assoc {:content-type :json
                            :throw-exceptions false
                            :headers {"Authorization" api-key}}
                           (if (= "get" http-action)
                             :query-params
                             :form-params)
                           params))]
      (if (< (:status response) 300)
        (assoc (parse-string (:body response) true)
               :status (:status response))
        {:status (:status response)
         :error  (str "Failed to call platform method. Body was: "
                      (:body response))}))))

(defn log
  [{:keys [level message data] :as args}]
  (call-platform-method
   {:platform-id (:patchwork-logger-platform-id env)
    :http-action "post"
    :platform-method level
    :params (dissoc args :level)}))
