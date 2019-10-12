(ns app.actions
  (:require [app.db :as db]
            [clojure.string :as str]))

(defn get! [table req]
  {:status 200
   :body (db/query [(str "SELECT resource FROM " (name table) " LIMIT 15")])})

(defn insert! [table {req :body}]
  {:status 201
   :body (db/insert table req)})

(defn update! [table {req :body}]
  {:status 200
   :body (db/update! table (dissoc req :created_at))})

(defn select! [table req]
  {:status 200
   :body (:resource (first (db/query [(str "SELECT resource FROM " (name table) " WHERE id = ?")
                                      (read-string (get-in req [:path-params :id]))])))})

(defn delete! [table req]
  {:status 200
   :body (db/delete! table (read-string (get-in req [:path-params :id])))})

(defn drop! [table]
  (db/do-commands
   (db/drop-table table)))

(comment
  (drop! :locations)
  (drop! :groups)
  (drop! :faculties))
