(ns app.migration
  (:require [app.actions :as a]
            (app.resources
             [schedule :as schedule]
             [student  :as student]
             [group    :as group]
             [user     :as user])))

(defn migrate [table]
  (when-not (a/-exists? (:table table)) (a/-create table)))

(defn migration []
  (migrate schedule/table)
  (migrate student/table)
  (migrate user/table)
  (migrate group/table))

(comment
  (a/-drop user/table)
  (a/-drop student/table)
  (a/-drop schedule/table)
  (a/-drop group/table))
