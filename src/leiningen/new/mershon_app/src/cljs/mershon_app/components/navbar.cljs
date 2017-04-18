(ns {{ns-name}}.components.navbar
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as d :include-macros true]

            [om-bootstrap.button :as b]
            [om-bootstrap.grid :refer [row col]]
            [om-bootstrap.nav :as n]

            [{{ns-name}}.state.app-state :as app-state]

            [{{ns-name}}.components.shared :as shared])
  (:require-macros [devcards.core :as dc :refer [defcard defcard-om do]]))

(defn- menu-item
  [nav route on-select-handler]
  (b/menu-item {:role "button"
                :style {:cursor "pointer"}
                :href nil
                :on-click #(on-select-handler (:path nav))}
               (:title nav)))
(defn- nav-item
  [nav route on-select-handler]
  (n/nav-item {:class (if (= (:path route) (:path nav)) "current")
               :role "button"
               :href nil
               :on-click #(on-select-handler (:path nav))}
              (:title nav)))

(defcomponent navbar
  [root-state :- {} owner {:keys [on-select-handler] :as opts}]
  (render
    [_]
    (let [role (get-in root-state [:session :role])]
      (n/navbar
       {:brand (d/a {:href "#"}
                    (d/img {:class "nav-logo"
                            :src "/images/nav-logo.png"
                            :alt "{{ns-name}}"}))}
       (n/nav
        {:collapsible? true}
        (when (not-empty (:session root-state))
          (map
           #(nav-item % (:route root-state) on-select-handler)
           [{:path "index"        :title "Dashboard"}
            ; other paths go here
            ]))

        :right
        (if (not-empty (:session root-state))
          (b/dropdown {:id "nav-right-menu"
                       :key "nav-drop"
                       :title (get-in root-state [:session :first_name])}
                      ; menu based on user permissions goes here
                      ;(when (not= "basic" role)
                      ;  (list
                      ;   (menu-item {:path "companies" :title "Switch Company"}
                      ;              (:route root-state) on-select-handler)
                      ;   (b/menu-item {:divider? true})))

                      (menu-item {:path "logout" :title "Logout"}
                                 (:route root-state) on-select-handler))
          (nav-item {:path "login" :title "Login"}
                    (:route root-state) on-select-handler)))))))

(dc/do ; GOTCHA -- this isn't a clojure do, but a macro from devcards...
  (let [root-atom (atom {:session {:first_name "Manager"
                                   :role "manager"}
                         :route {:path "index"}})]
    (defcard-om navbar-card
      "*{{ns-name}} standard navbar*

      This component requires the root app state

      ```
      (om/build navbar app-state/state
                       {:opts {:on-select-handler routes/nav!}})
      ```"
      navbar
      root-atom
      {:opts {:on-select-handler
              (fn [path]
                (swap! root-atom assoc-in [:route :path] path)
                (when (= path "logout")
                  (swap! root-atom assoc :session {})))}}
      {:inspect-data true :history true})
    (defcard
      (fn []
        (let [do-nav! #(swap! root-atom assoc :session %)]
          (d/div
            (b/button {:on-click #(do-nav! {})}
                      "log out")
            (b/button {:on-click #(do-nav! {:first_name "Manager"
                                            :role "manager"})}
                      "log in manager")
            (b/button {:on-click #(do-nav! {:first_name "Basic"
                                            :role "basic"})}
                      "log in basic")))
        )
      []
      {})))
