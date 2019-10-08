(ns migration
  (:require (resources
             [location :as location]
             [group    :as group])))

(defn migrated? [table]
  (-> (db/query (str "select count(*) from information_schema.tables "
                     "where table_name='"(name table)"'"))
      first :count pos?))

(defn migrate [table table-ddl]
  (when (not (migrated? table))
    (print "[" (name table) "]: Creating ") (flush)
    (db/do-commands
     (db/create-table table
                      table-ddl))
    (println "[OK]")))

(defn migration []
  (do
    (migrate :groups     group/table)
    (migrate :locations location/table)))
