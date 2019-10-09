(ns app.resources.faculty)

(def table
  [[:id :serial   "PRIMARY KEY"]
   [:name         "varchar(64)"]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])
