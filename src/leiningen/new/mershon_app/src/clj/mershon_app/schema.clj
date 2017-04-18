(ns {{ns-name}}.schema
  (:use [clj-liquibase.core :refer (defparser)])
  (:require [environ.core :refer [env]]
            [clj-dbcp.core :as cp]
            [clj-liquibase.cli :as cli]
            [yesql.core :refer [defqueries]]))

(def db-spec  {:classname "org.postgresql.Driver"
               :subprotocol "postgresql"
               :subname (str "//" (env :rds-hostname)
                             ":"  (env :rds-port)
                             "/"  (env :rds-db-name))
               :user (env :rds-username)
               :password (env :rds-password)})

; add query files here
;(defqueries "queries/user.sql"    {:connection db-spec})

(defparser app-changelog "changelog.xml")

(def ds
  (cp/make-datasource :postgresql {:host     (env :rds-hostname)
                                   :port     (env :rds-port)
                                   :database (env :rds-db-name)
                                   :user     (env :rds-username)
                                   :password (env :rds-password)}))

(defn do-update!
  []
  (apply cli/entry "update" {:datasource ds :changelog app-changelog} []))

(defn -main
  [& [cmd & args]]
  (apply cli/entry cmd {:datasource ds :changelog app-changelog}
         args))
