(ns resources.location
  (:require [db :as db]))

(def table-ddl
  [[:id :serial "PRIMARY KEY"]
   [:building "varchar(32)"]
   [:number  "varchar(16)"]
   [:slots  "varchar(8)"]
   [:responsible  "varchar(32)"]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])

;GET /locations
(defn get! []
  (db/query "SELECT * FROM locations"))

                                        ;POST /locations
(defn insert! [{req :body}]
  (db/insert :locations
             (select-keys req [:building :number :slots :responsible])))

(defn drop! []
  (db/do-commands
   (db/drop-table :locations)))

