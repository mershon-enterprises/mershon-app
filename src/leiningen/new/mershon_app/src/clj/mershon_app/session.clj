(ns {{ns-name}}.session
  (:require [buddy.auth.accessrules :refer [restrict success error]]
            [buddy.auth.backends :as backends]
            [buddy.sign.jwt :as jwt]

            [environ.core :refer [env]]))

(def auth-secret (:auth-secret env))

(def auth-strategy
  (backends/jws {:secret auth-secret}))

(defn authenticated-user
  [request]
  (if (:identity request)
    true
    (error "Only authenticated users allowed")))
(defn same-user
  [user-uid request]
  (if (= user-uid (get-in request [:identity :user :uid]))
    true
    (error "Only the account owner allowed")))

(defn if-authenticated?
  [fn-name & args]
  (restrict
   (fn [req]
     (apply fn-name args))
   {:handler authenticated-user
    :on-error {:status 401
               :body {:message "You must be logged into do that"}}}))
(defn if-authenticated-admin?
  [fn-name & args]
  (restrict
   (fn [req]
     (apply fn-name args))
   {:handler admin-user
    :on-error {:status 403
               :body {:message "Access denied"}}}))
(defn if-me?
  [user-uid fn-name & args]
  (restrict
   (fn [req]
     (apply fn-name args))
   {:handler #(same-user user-uid %)
    :on-error {:status 403
               :body {:message "Access denied"}}}))
