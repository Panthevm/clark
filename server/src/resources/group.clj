(ns resources.group)

(def table
  [[:id :serial "PRIMARY KEY"]
   ;[:faculty :serial "REFERENCES faculty"]
   [:department "varchar(64)"]
   [:course "varchar(1)"]
   [:students_number "varchar(2)"]
   [:created_at :timestamp
    "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]])
