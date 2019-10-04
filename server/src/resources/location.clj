(ns resources.location
  (:require [db :as db]))

(def table-ddl
  [[:id :serial "PRIMARY KEY"]
   [:building "varchar(32)"]
   [:number :int]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])

;GET /locations
(defn get []
  (db/query "SELECT * FROM locations"))

;POST /locations
(defn insert [building number]
  (db/insert :locations {:building building
                         :number number}))

(defn drop []
  (db/do-commands
   (db/drop-table :locations)))

