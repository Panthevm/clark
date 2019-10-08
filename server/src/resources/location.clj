(ns resources.location
  (:require [db :as db]
            [clojure.string :as str]))

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
  (db/query ["SELECT * FROM locations LIMIT 30"]))

                                        ;POST /locations
(defn insert! [{req :body}]
  (db/insert :locations
             (select-keys req [:building :number :slots :responsible])))
                                        ;PUT /locations
(defn update! [{req :body}]
  (db/update! :locations req))

                                        ;GET /locations/:id
(defn select! [id]
  (db/query ["SELECT * FROM locations WHERE id = ?" (read-string id)]
            {:result-set-fn first}))
                                        ;DELETE /locations/:id
(defn delete! [id]
  (db/delete! :locations (read-string id)))

(defn drop! []
  (db/do-commands
   (db/drop-table :locations)))

