(ns {{ns-name}}.routes.version
  (:use [ring.util.response])
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all])
  (:import (java.util Properties)))

(defn read-project-version [groupid artifact]
  (-> (doto (Properties.)
        (.load (-> "META-INF/maven/%s/%s/pom.properties"
                   (format groupid artifact)
                   (io/resource)
                   (io/reader))))
      (.get "version")))

(defn get-app-version
  []
  (apply read-project-version (repeat 2 "{{ns-name}}")))

(defn get-version
  []
  (response {:version (get-app-version)}))

(def version-routes (GET "/version" [] (get-version)))
