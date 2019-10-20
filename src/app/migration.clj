(ns app.migration
  (:require [app.actions :as a]
            (app.resources
             [schedule :as schedule]
             [group    :as group])))

(defn migrate [table]
  (when (not (a/-exists? (:table table)))
    (a/-create table)))

(defn migration []
  (do
    (migrate schedule/table)
    (migrate group/table)))

(comment
  (a/-drop schedule/table)
  (a/-drop group/table))
