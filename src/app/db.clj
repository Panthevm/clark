(ns app.db
  (:use app.honey-extension)
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as json]
            [honeysql.helpers :as h]
            [honeysql.core     :as sql])
  (:import org.postgresql.util.PGobject))


(def pg-db {:dbtype "postgresql"
            :dbname "clark"
            :user "admin"
            :password "admin"
            :host "localhost"
            :port 5432})

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value]
    (doto (PGobject.)
      (.setType "jsonb")
      (.setValue (json/write-str value)))))

(extend-protocol jdbc/IResultSetReadColumn
  PGobject
  (result-set-read-column [pgobj metadata idx]
    (let [type  (.getType pgobj)
          value (.getValue pgobj)]
      (case type
        "jsonb" (json/read-str value :key-fn keyword)
        :else value))))

(defn jsonb-object [value]
  (doto (PGobject.)
    (.setType "jsonb")
    (.setValue (json/write-str value))))

(extend-protocol jdbc/ISQLValue
  clojure.lang.IPersistentMap
  (sql-value [value] (jsonb-object value))

  clojure.lang.IPersistentVector
  (sql-value [value] (jsonb-object value)))

(defn query
  ([query]
   (jdbc/query pg-db (sql/format query)))
  ([query attrs]
   (jdbc/query pg-db (sql/format query) attrs)))

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
