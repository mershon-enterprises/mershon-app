(ns {{ns-name}}.helpers.shared
  (:require [goog.string :as gstring]))

(defn zero-pad
  [n]
  (.slice (str "00" n) -2))

(defn as-number
  [v]
  (let [n (js/Number. v)]
    (if (or (pos? n)
            (neg? n))
      n
      0)))

(defn format-date
  [date]
  (str (zero-pad (inc (.getMonth date))) "/"
       (zero-pad (.getDate date)) "/"
       (.getFullYear date)))

(defn days-from-now
  [days]
  (let [now (-> (js/Date.) (.getTime))
        then (+ now (* 1000 60 60 24 days))]
    (js/Date. then)))

(defn round-to
  [n d]
  (.toFixed (/ (js/Math.round (* (js/Math.pow 10 d) n))
               (js/Math.pow 10 d)) d))

(defn format-money
  [amount]
  (let [v (as-number amount)]
    (gstring/format "%.2f" v)))

(defn- split-date [date]
  (str (zero-pad (inc (.getMonth date)))
       "/"
       (zero-pad (.getDate date))
       "/"
       (.getFullYear date)))

(defn not-zero
  [v]
  (when (pos? v) v))

(defn today
  []
  (let [now (js/Date.)]
    (str (.getFullYear now)
         "-"
         (inc (.getMonth now))
         "-"
         (.getDate now))))

(defn before-today?
  [iso-date-string]
  (let [then (js/Date. iso-date-string)]
    (pos? (- (.getTime (js/Date.))
             (.getTime then)))))

(defn position-of
  [needle haystack key-fn]
  (first (keep-indexed
          (fn [idx b]
            (if (= (key-fn b) (key-fn needle))
              idx))
          haystack)))
