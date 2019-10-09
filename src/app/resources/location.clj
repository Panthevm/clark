(ns app.resources.location)

(def table
  [[:id :serial   "PRIMARY KEY"]
   [:building     "varchar(32)"]
   [:number       "varchar(16)"]
   [:slots        "varchar(8)"]
   [:responsible  "varchar(32)"]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])
