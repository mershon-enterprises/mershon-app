(ns {{ns-name}}.components.login
  (:require [cljs.spec :as s]

            [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as d :include-macros true]

            [om-bootstrap.button :as b]
            [om-bootstrap.input :as i]

            [{{ns-name}}.components.shared :as shared]

            [{{ns-name}}.spec.user :as spec]

            [{{ns-name}}.state.app-state :as app-state])
  (:require-macros [devcards.core :as dc :refer [defcard defcard-om]]))

(defcomponent login-form
  [login-cursor :- {} owner {:keys [login-handler] :as opts}]
  (render
    [_]
    (d/div
      (d/h3 "Login")
      (i/input
        {:label     "Email Address"
         :name      "email"
         :type      "text"
         :help      ""
         :value     (:user_email login-cursor)
         :on-change (shared/value-setter! login-cursor :user_email)
         :addon-button-after
         (b/button {:bs-style "success"
                    :on-click #(login-handler login-cursor)
                    :disabled? (not (s/valid? ::spec/email
                                              (:user_email login-cursor)))}
                   (d/i {:class "glyphicon glyphicon-send"})
                   " Send Magic Link")}))))

(defcard-om login-form
  "*login form*

  ```
  (om/build login-form (om/observe owner (app-state/key-cursor :login))
                       {:opts {:login-handler helpers/do-login!}})
  ```"
  login-form
  (atom {:user_email  ""})
  {:opts {:login-handler (fn [credentials] (js/alert "override login"))}}
  {:inspect-data true :history true})
