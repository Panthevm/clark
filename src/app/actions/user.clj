(ns app.actions.user
  (:require [app.db        :refer [db]]
            [clj-pg.honey  :as pg]
            [cheshire.core :as json]))

(defn -get [username]
  (pg/query-first (db)
                  {:select [:*]
                   :from   [:user]
                   :where  ["@>" :resource
                            (json/generate-string {:username username})]}))
