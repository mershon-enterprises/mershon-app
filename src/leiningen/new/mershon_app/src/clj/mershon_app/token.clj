(ns {{ns-name}}.token
  (:require [clojure.string :refer [join]]))

(defn generate-token
  ([]
   (generate-token 80))
  ([length]
   ; GOTCHA - Capital I and Lowercase L are removed for similarity to 1,
   ;          and Capital and Lowercase O are removed for similarity to 0
   ;          so if for some horrible reason people have to dictate API tokens
   ;          aloud, they won't mix up characters
   (let [chrs "0123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz"
         token (take length (repeatedly #(rand-nth chrs)))]
     (join token))))
