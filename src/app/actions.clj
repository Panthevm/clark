(ns app.actions
  (:require [app.db :as db]
            [clojure.string :as str]))

(defn get! [table req]
  {:status 200
   :body (db/query [(str "SELECT * FROM " (name table) " LIMIT 15")])})

(defn insert! [table {req :body}]
  {:status 201
   :body (db/insert table
                    (dissoc req :created_at))})

(defn update! [table {req :body}]
  {:status 200
   :body (db/update! table (dissoc req :created_at))})

(defn select! [table req]
  {:status 200
   :body (db/query [(str "SELECT * FROM " (name table) " WHERE id = ?")
                    (read-string (get-in req [:path-params :id]))]
                   {:result-set-fn first})})

(defn delete! [table req]
  {:status 200
   :body (db/delete! table (read-string (get-in req [:path-params :id])))})

(defn drop! [table]
  (db/do-commands
   (db/drop-table table)))
