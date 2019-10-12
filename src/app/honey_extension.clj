(ns app.honey-extension
  (:require
   [honeysql.format :as f]
   [honeysql.helpers :as h]))

(defmethod f/format-clause :returning [[_ fields] _]
  (str "RETURNING " (f/comma-join (map f/to-sql fields))))

(f/register-clause! :returning 225)

(h/defhelper returning [m fields]
  (assoc m :returning (h/collify fields)))
