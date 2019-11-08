(ns app.resources.student)

(def table
  {:table   :student
   :columns {:id            {:type :serial :primary true :weighti 0}
             :resource      {:type :jsonb}}})
