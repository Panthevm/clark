(ns app.actions.report
  (:require [app.db       :refer [db]]
            [clj-pg.honey :as pg]
            [app.actions  :as a]))

(defn -get
  [{{:keys [subject journal]} :params}]
  (let [x 1 ] x))

(def sql
  "SELECT x
   FROM (SELECT
           jsonb_array_elements_text(resource->'schedule'->??????)
         FROM schedule
         WHERE id = 18) as x(v)
   ") ;TODO
(comment
  (let [subject (pg/query (db) {:select [:resource] :from [:student] :where [:= 4 :id]})
        journal (pg/query (db) {:select [:resource] :from [:schedule] :where [:= 18 :id]})
        avg     (pg/query (db) sql)
        ]))
