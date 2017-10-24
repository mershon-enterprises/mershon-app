(ns {{ns-name}}.state.app-state
  (:require [ajax.core :refer [default-interceptors to-interceptor]]
            [om.core :as om :include-macros true]))

(defonce state (atom {:route   {:path "index"}
                      :session {}
                      :login   {}}))

(defn key-cursor [key-name]
  (om/ref-cursor (get (om/root-cursor state) key-name)))

; call this to reload from local storage
(defn- reload-local-storage!
  []
  (when-let [sess-json (.getItem js/localStorage "session")]
    (let [parsed (js/JSON.parse sess-json)
          sess (js->clj parsed :keywordize-keys true)]
      (swap! state assoc :session sess))))

(defn- write-session!
  []
  (doto js/localStorage
    (.setItem "session" (js/JSON.stringify (clj->js (:session @state))))))

(defn set-session!
  [sess]
  (let [token (get-in @state [:session :token])
        merged-session (assoc sess :token token)]
    (swap! state assoc :session merged-session)
    (write-session!)))

(defn clear-session!
  []
  (swap! state assoc :session {})
  (.clear js/localStorage))

(def token-interceptor
  (to-interceptor {:name "Token Interceptor"
                   :request (fn [request]
                              (let [token (get-in @state [:session :token])
                                    headers (:headers request)
                                    with-token (assoc headers
                                                      :authorization (str "Token " token))]
                                (assoc request :headers with-token)))}))
(swap! default-interceptors (partial cons token-interceptor))

(def JSON-headers-format
  {:read (fn [xhrio]
           (let [json (js/JSON.parse (.getResponseText xhrio))
                 ; GOTCHA -- some servers/browsers return header keys uppercased
                 headers (reduce-kv
                           (fn [m k v]
                             (assoc m (.toLowerCase k) v))
                           {}
                           (js->clj (.getResponseHeaders xhrio)))
                 token (get headers "token")]

             ; only update the token when one is returned
             (when token
               (swap! state assoc-in [:session :token] token)
               (write-session!))
             (js->clj json :keywordize-keys true)))
   :description "JSON-headers"})

(reload-local-storage!)
