(ns resources.group
  (:require [db :as db]
            [clojure.string :as str]))

(def table
  [[:id :serial "PRIMARY KEY"]
   [:department  "varchar(64)"]
   [:course  :int2]
   [:studentsNumber :int2]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])

                                        ;GET /group
(defn get! []
  (db/query ["SELECT * FROM groups LIMIT 15"]))

                                        ;POST /group
(defn insert! [{req :body}]
  (db/insert :locations
             (select-keys req [:faculty :department :course :studentsNumber])))
                                        ;PUT /group
(defn update! [{req :body}]
  (db/update! :group req))

                                        ;GET /group/:id
(defn select! [id]
  (db/query ["SELECT * FROM group WHERE id = ?" (read-string id)]
            {:result-set-fn first}))
                                        ;DELETE /group/:id
(defn delete! [id]
  (db/delete! :group (read-string id)))

(defn drop! []
  (db/do-commands
   (db/drop-table :group)))
