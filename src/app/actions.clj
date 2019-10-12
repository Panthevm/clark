(ns app.actions
  (:require [app.db         :as db]
            [clojure.data.json :as json]
            [honeysql.helpers :as h]
            [clojure.string :as str]))

(defn get! [table req]
  {:status 200
   :body   (db/query {:select [:resource]
                      :from   [table]
                      :limit  15})})

(defn insert! [table {req :body}]
  (let [{:keys [id resource]} (db/query {:insert-into [[table [:resource]]
                                          {:values [[(db/jsonb-object req)]]}]
                            :returning [:*]}
                           {:result-set-fn first})
        resp (db/query {:update table
                        :set {:resource
                              (db/jsonb-object
                               (assoc resource
                                      :id id
                                      :resource_type (name table)))}
                        :where [:= :id id]
                        :returning [:resource]}
                       {:result-set-fn first})]
    {:status 201
     :body resp}))

(defn update! [table {req :body}]
  {:status 200
   :body (db/query {:update table
                    :set {:resource (db/jsonb-object req)}
                    :where [:= :id (:id req)]
                    :returning [:resource]})})

(defn select! [table {{id :id} :path-params}]
  {:status 200
   :body   (db/query {:select [:resource]
                      :from   [table]
                      :where  [:= :id (read-string id)]}
                     {:result-set-fn first})})

(defn delete! [table {{id :id} :path-params}]
  {:status 200
   :body (db/delete! table (read-string id))})

(defn drop! [table]
  (db/do-commands
   (db/drop-table table)))

(comment
  (drop! :locations)
  (drop! :groups)
  (drop! :faculties))
