(ns {{ns-name}}.routes
  (:require [secretary.core :as secretary :refer-macros [defroute]]
            [goog.events :as events]
            [goog.history.EventType :as EventType]

            [om.core :as om :include-macros true]

            ; add AJAX helpers here

            [{{ns-name}}.state.app-state :refer [state clear-session!]])

  (:import goog.history.Html5History))

; enable HTML5 history support
(defonce history
  (doto (Html5History.)
    (goog.events/listen EventType/NAVIGATE
                        #(-> % .-token secretary/dispatch!))
    (.setUseFragment false)
    (.setPathPrefix "")
    (.setEnabled true)))

(defn nav! [token]
  (.setToken history token))

(defn- session-guard
  [callable]
  (let [session (:session @state)
        now (-> (js/Date.) (.getTime))
        expiration-date (-> (js/Date. (get-in session
                                              [:token :expiration-date]))
                            (.getTime))]
    (if (or (empty? session)
            (not (pos? (- expiration-date now))))
      (nav! "/logout")
      (callable))))

(defroute index "/" []
  (swap! state assoc :route {:path "index"})
  ; FIXME -- uncomment to enable session restrictions
  #_(session-guard
     (fn []
       (let [session (:session @state)]
         (swap! state assoc :route {:path "index"})))))

(defroute index-route "/index" []
  (swap! state assoc :route {:path "index"}))

(defroute login "/login" []
  (swap! state assoc :route {:path "login"
                             :login {:user_email  ""
                                     :access_code ""}}))

(defroute logout "/logout" []
  (clear-session!)
  (nav! "/login")
  (js/window.location.reload))

(defroute contact-us "/contact-us" []
  (swap! state assoc :route {:path "contact-us"}))
(defroute terms-of-use "/terms-of-use" []
  (swap! state assoc :route {:path "terms-of-use"}))

; route to the appropriate place, on load
(secretary/dispatch! js/window.location.pathname)
