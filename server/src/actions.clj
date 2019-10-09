(ns actions
  (:require [db :as db]
            [clojure.string :as str]))

(defn get! [table]
  (db/query [(str "SELECT * FROM " (name table) " LIMIT 15")]))

(defn insert! [table {req :body}]
  (db/insert table
             (dissoc req :created_at)))

(defn update! [table {req :body}]
  (db/update! table req))

(defn select! [table id]
  (db/query [(str "SELECT * FROM " (name table) " WHERE id = ?") (read-string id)]
            {:result-set-fn first}))

(defn delete! [table id]
  (db/delete! table (read-string id)))

(defn drop! [table]
  (db/do-commands
   (db/drop-table table)))
