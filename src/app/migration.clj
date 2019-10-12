(ns app.migration
  (:require [app.db :as db]
            (app.resources
             [location :as location]
             [group    :as group]
             [faculty  :as faculty])
            [clojure.string :as str]))

(defn default-rows
  [table table-ddl]
  (into []
        (concat table-ddl [[:id :serial :primary :key]
                           [:resource_type :text
                            "NOT NULL" (str "DEFAULT TEXT '" (name table)"'")]
                           [:created_at :timestamp
                            "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])))

(defn migrated? [table]
  (-> (db/query {:select [:%count.*]
                 :from   [:information_schema.tables]
                 :where  [:= :table_name (str (name table))]})
      first :count pos?))

(defn migrate [table table-ddl]
  (when (not (migrated? table))
    (print "[" (name table) "]: Creating ") (flush)
    (db/do-commands
     (db/create-table table
                      (default-rows table table-ddl)))
    (println "[OK]")))

(defn migration []
  (do
    (migrate :faculties faculty/table)
    (migrate :groups    group/table)
    (migrate :locations location/table)))
