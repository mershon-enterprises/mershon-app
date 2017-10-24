(ns {{ns-name}}.helpers
  (:require [ring.util.response :refer [response]]
            [environ.core :refer [env]]))

(defonce app-url
  (str (:hostproto env)
       "://"
       (:hostname env)
       (if (:is-dev? env)
         ":3449")))

(defn uidify
  [uid-string]
  (->
    ; lowercase
    (.toLowerCase uid-string)
    ; strip out non-alphanumeric characters, leaving spaces
    (clojure.string/replace #"[^a-z0-9 ]" "")
    ; strip out repeated spaces
    (clojure.string/replace #"[ ]+" " ")
    ; dash-case
    (clojure.string/replace #" " "-")))
