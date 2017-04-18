(ns {{ns-name}}.spec.user
  #?(:clj
      (:require [clojure.spec :as s]))
  #?(:cljs
      (:require [cljs.spec :as s])))

(s/def ::uid        (s/and string? not-empty #(<= (count %) 80)))
(s/def ::first_name (s/and string? not-empty #(<= (count %) 255)))
(s/def ::last_name  (s/and string? not-empty #(<= (count %) 255)))
; emails have at least 3 characters and exactly 1 "@" symbol
(s/def ::email      (s/and string?
                           #(>= (count %) 3)
                           #(<= (count %) 255)
                           #(= (get (frequencies %) \@) 1)))
(s/def ::role #{"admin" "manager" "basic"})

; new users don't have a uid
(s/def ::{{ns-name}}.spec.user/new-user
  (s/keys :req-un [::first_name ::last_name ::email ::role]))

; created/existing users have a uid
(s/def ::{{ns-name}}.spec.user/existing-user
  (s/keys :req-un [::uid ::first_name ::last_name ::email ::role]))
