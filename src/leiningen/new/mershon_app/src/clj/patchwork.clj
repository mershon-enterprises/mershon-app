(ns patchwork
  (:require [environ.core :refer [env]]
            [clj-http.client :as client]
            [cheshire.core :refer [parse-string]]))

(defonce config (atom {}))

(defn init
  [{:keys [base-url application token] :as args}]
  (reset! config args))
(init {:base-url    (:patchwork-base-url    env)
       :application (:patchwork-application env)
       :token       (:patchwork-token       env)})

(defn call-platform-method
  [{:keys [platform http-action platform-method params] :as args}]

  (let [{base-url    :base-url
         application :application
         token       :token} @config
        callable             (case (.toLowerCase http-action)
                               "post"   client/post
                               "get"    client/get
                               "put"    client/put
                               "delete" client/delete)]
    (let [response (callable
                    (str base-url         "/api"
                         "/applications/" application
                         "/"              platform
                         "/"              platform-method)
                    (assoc {:content-type :json
                            :throw-exceptions false
                            :headers {"Authorization" token}}
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
   {:platform (:patchwork-logger-platform-uid env)
    :http-action "post"
    :platform-method level
    :params {:message message
             :data data}}))
