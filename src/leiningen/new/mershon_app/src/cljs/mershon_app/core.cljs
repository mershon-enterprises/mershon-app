(ns {{ns-name}}.core
  (:require [om.core :as om :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as d :include-macros true]

            [{{ns-name}}.routes :refer [nav!]]

            [{{ns-name}}.components.navbar :refer [navbar]]

            [{{ns-name}}.pages.login :as login-page]

            [{{ns-name}}.state.app-state :as app-state]

            ; XXX - direct widget references required for devcards!
            [{{ns-name}}.components.login]))

(enable-console-print!)

(defcomponent root-component
  [root-state :- {} owner]
  (render
    [_]
    (let [route (om/observe owner (app-state/key-cursor :route))]
      (d/div
        {:id "container"}
        (om/build navbar root-state {:opts {:on-select-handler nav!}})
        (d/div {:id "body-div"}
               (case (:path route)
                 "index"
                 (d/div (d/h1 "Hello {{ns-name}}"))

                 "login"
                 (om/build login-page/main {})))
        (d/footer
          (d/p "© 2017 {{ns-name}} All Rights Reserved."))))))

(when-let [container (js/document.getElementById "app")]
 (om/root
   root-component
   app-state/state
   {:target (js/document.getElementById "app")}))
