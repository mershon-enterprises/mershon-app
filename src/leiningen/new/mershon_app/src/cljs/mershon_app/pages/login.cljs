(ns {{ns-name}}.pages.login
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]

            [{{ns-name}}.components.login :refer [login-form]]

            [{{ns-name}}.helpers.login :as helpers]

            [{{ns-name}}.state.app-state :as app-state])
  (:require-macros [devcards.core :as dc :refer [defcard defcard-om]]))

(defcomponent main
  [data :- {} owner]
  (render
    [_]
    (let [login-cursor (om/observe owner (app-state/key-cursor :login))]
      (om/build login-form
                login-cursor
                {:opts {:login-handler helpers/do-login!}}))))
