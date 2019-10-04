(ns resources.location
  (:use ring.util.response)
  (:require [db :as db]
            [compojure.core :refer :all]))

;Shema
(def table-ddl
  (db/create-table :locations [[:building "varchar(32)"]
                               [:number :int]]))
;GET /locations
(defn get []
  (db/query "SELECT * FROM locations"))

;POST /locations
(defn insert [building number]
  (db/insert :locations {:building building
                         :number number}))


