(ns app.resources.group)

(def table
  {:table   :group
   :columns {:id            {:type :serial :primary true :weighti 0}
             :resource      {:type :jsonb}}})
