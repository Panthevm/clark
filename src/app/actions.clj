(ns app.actions
  (:require [app.db :refer [db]]
            [clj-pg.honey :as pg]))

(defn -exists? [table] (pg/table-exists? (db) table))
(defn -create  [table] (pg/create-table  (db) table))
(defn -drop    [table] (pg/drop-table    (db) table))

(defn -get [table req]
  {:status  200
   :body    (pg/query (db) {:select [:resource] :from [(:table table)]})})

(defn -post [table {body :body}]
  (let [insert   (pg/create (db) table {:resource body})
        response (pg/update (db) table
                            (update insert :resource
                                    #(assoc %
                                            :resource_type (-> table :table name)
                                            :id (:id insert))))]
    {:status 201
     :body   (select-keys response [:resource])}))

(defn -select [table {{id :id} :path-params :as ss}]
  (let [response (pg/query-first (db) {:select [:resource]
                                       :from [(:table table)]
                                       :where [:= :id id]})]
    {:status 200
     :body response}))

(defn -put
  [table {:keys [body path-params]}]
  (let [response (pg/update (db) table {:id (:id path-params)
                                        :resource body})]
    {:status 200
     :body (:resource response)}))

(defn -delete
  [table {{id :id} :path-params}]
  (let [response (pg/delete (db) table id)]
    {:status 200
     :body (select-keys response [:id])}))

