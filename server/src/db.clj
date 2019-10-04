(ns db
  (:require [clojure.java.jdbc :as jdbc]))

(def pg-db {:dbtype "postgresql"
            :dbname "clark"
            :host "localhost"
            :port 5432})

(defn query [query]
  (jdbc/query pg-db [query]))

(defn insert [table data]
  (first
   (jdbc/insert! pg-db table data)))

(defn create-table [table schema]
  (jdbc/create-table-ddl table schema))

(defn drop-table [table]
  (jdbc/drop-table-ddl table))

(defn do-commands [fn]
  (jdbc/db-do-commands pg-db fn))
