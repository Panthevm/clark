(ns app.resources.user)

(def table
  {:table   :user
   :columns {:id            {:type :serial :primary true :weighti 0}
             :resource      {:type :jsonb}}})
