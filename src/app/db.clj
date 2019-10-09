(ns app.db
  (:require [clojure.java.jdbc :as jdbc]))

(def pg-db {:dbtype "postgresql"
            :dbname "clark"
            :host "localhost"
            :port 5432})

(defn query
  ([query]
   (jdbc/query pg-db query))
  ([query attrs]
   (jdbc/query pg-db query attrs)))

(defn insert [table data]
  (first
   (jdbc/insert! pg-db table data)))

(defn update! [table data]
  (let [is-updated (first
                    (jdbc/update! pg-db table data
                                  ["id = ?" (:id data)]))]
    (when is-updated data)))

(defn delete! [table id]
  (let [is-deleted (first
                    (jdbc/delete! pg-db table
                                  ["id = ?" id]))]
    (when is-deleted {:id id})))

(defn create-table [table schema]
  (jdbc/create-table-ddl table schema))

(defn drop-table [table]
  (jdbc/drop-table-ddl table))

(defn do-commands [fn]
  (jdbc/db-do-commands pg-db fn))
