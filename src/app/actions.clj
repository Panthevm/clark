(ns app.actions
  (:require [app.db :refer [db]]
            [honeysql.core :as hsql]
            [clj-pg.honey :as pg]))

(defn ok [response] {:status 200 :body response})

(defn -exists? [table] (pg/table-exists? (db) table))
(defn -create  [table] (pg/create-table  (db) table))
(defn -drop    [table] (pg/drop-table    (db) table))

(defn -get [table {params :params}]
  (let [q (:ilike params)]
    (ok (pg/query (db)
                  (merge {:select [:resource] :from [(:table table)]}
                         (when q {:where [:ilike (hsql/raw "resource::text") (str \% q \%)]}))))))

(defn -post [table {body :body}]
  (let [insert   (pg/create (db) table {:resource body})
        response (pg/update (db) table
                            (update insert :resource
                                    #(assoc %
                                            :resource_type (-> table :table name)
                                            :id (:id insert))))]
    {:status 201
     :body   response}))

(defn -select [table {{id :id} :path-params :as ss}]
  (let [response (pg/query-first (db) {:select [:resource]
                                       :from [(:table table)]
                                       :where [:= :id id]})]
    (ok response)))

(defn -put
  [table {:keys [body path-params]}]
  (let [response (pg/update (db) table {:id (:id path-params)
                                        :resource body})]
    (ok response)))

(defn -delete
  [table {{id :id} :path-params}]
  (let [response (pg/delete (db) table id)]
    (ok (:id response))))
