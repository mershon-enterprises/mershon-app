(ns {{ns-name}}.components.shared
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]

            [om-tools.core :refer-macros [defcomponent]]
            [om-tools.dom :as d :include-macros true]

            [om-bootstrap.button :as b]
            [om-bootstrap.grid :refer [row col]]
            [om-bootstrap.input :as i]

            [cljs.spec :as s]))

(defn set-value!
  "Helper method for updating state on a specfic key/value pair on a component"
  [cursor k v]
  (om/update! cursor k v))

(defn value-setter!
  "Helper method for updating state on a component"
  [cursor k & {:keys [int? float?] :or {int?   false
                                        float? false}}]
  (fn [ev]
    (set-value! cursor k (let [v (.. ev -target -value)]
                           (cond
                             int?
                             (js/parseInt v)
                             float?
                             (js/parseFloat v)
                             :else
                             v)))))

(s/def ::label (s/and string? #(pos? (count %))))
(s/def ::value string?)
(s/def ::option-args (s/keys :req-un [::label ::value]))
(defn option
  [{:keys [value label] :as args}]
  {:pre [(if-not (s/valid? ::option-args args)
           (do
             (s/explain ::option-args args)
             true)
           true)]}
  (apply dom/option
         #js{:key   (str "opt-" value)
             :value value}
         label))

(defn select
  [{:keys [id class on-change default-value]} body]
  (apply dom/select
         #js{:id        id
             :className class
             :value     (or default-value "undefined")
             :onInput   on-change}
         body))

(defn filter-choice
  [choice filter-handler]
  (b/menu-item {:key (keyword (:filter choice))
                :style {:cursor "pointer"}
                :role "button"
                :href nil
                :on-click #(filter-handler (:filter choice))}
               (:label choice)))
