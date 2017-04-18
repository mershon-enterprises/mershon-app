 (ns {{ns-name}}.helpers.login
   (:require [ajax.core :refer [POST]]

             [{{ns-name}}.config :refer [app-url]]

             [{{ns-name}}.routes :refer [nav!]]

             [{{ns-name}}.state.app-state :as app-state]))

(defn do-login!
  [credentials]
  (POST (str (app-url) "/api/magic-link")
        {:response-format app-state/JSON-headers-format
         :format :json
         :params credentials
         :keywords? true
         :error-handler
         (fn [error]
           (js/console.warn (str "DEBUG - login failed. Reason: " error)))
         :handler
         (fn [user]
           (swap! app-state/state assoc :login {}))}))
