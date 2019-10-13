(ns app.migration
  (:require [app.actions :as a]
            (app.resources
             [group    :as group])))

(defn migrate [table]
  (when (not (a/-exists? (:table table)))
    (a/-create table)))

(defn migration []
  (migrate group/table))
