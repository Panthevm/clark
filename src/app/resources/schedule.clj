(ns app.resources.schedule)

(def table
  {:table   :schedule
   :columns {:id            {:type :serial :primary true :weighti 0}
             :resource      {:type :jsonb}}})
