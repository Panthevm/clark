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
(defn insert! [building number slots responsible]
  (db/insert :locations {:building    building
                         :number      number
                         :slots       slots
                         :responsible responsible}))

(defn drop! []
  (db/do-commands
   (db/drop-table :locations)))

