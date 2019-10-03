(ns db
  (:require [clojure.java.jdbc :as jdbc]))

(def pg-db {:dbtype "postgresql"
            :dbname "clark"
            :host "localhost"})

(def location-table-ddl
  (jdbc/create-table-ddl :locations [[:building "varchar(32)"]
                                     [:number :int]]))

(comment
  ;Create table
  (jdbc/db-do-commands pg-db location-table-ddl)

  ;Create data
  (jdbc/insert! pg-db :locations {:building "Г" :number 406})

  ;Get data
  (jdbc/query pg-db ["SELECT * FROM locations"]))
