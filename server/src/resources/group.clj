(ns resources.group
  (:require [db :as db]
            [clojure.string :as str]))

(def table
  [[:id :serial "PRIMARY KEY"]
   ;[:faculty :serial "REFERENCES faculty"]
   [:department "varchar(64)"]
   [:course "varchar(1)"]
   [:students_number "varchar(2)"]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])

                                        ;GET /group
(defn get! []
  (db/query ["SELECT * FROM groups LIMIT 15"]))

                                        ;POST /group
(defn insert! [{req :body}]
  (db/insert :groups
             (select-keys req [:department :course :students_number])))
                                        ;PUT /group
(defn update! [{req :body}]
  (db/update! :groups req))

                                        ;GET /group/:id
(defn select! [id]
  (db/query ["SELECT * FROM groups WHERE id = ?" (read-string id)]
            {:result-set-fn first}))
                                        ;DELETE /group/:id
(defn delete! [id]
  (db/delete! :groups (read-string id)))

(defn drop! []
  (db/do-commands
   (db/drop-table :groups)))
