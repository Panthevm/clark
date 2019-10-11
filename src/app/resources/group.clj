(ns app.resources.group)

(def table
  [[:faculty :integer]
   [:department "varchar(64)"]
   [:course "varchar(1)"]
   [:students_number "varchar(2)"]
   ["FOREIGN KEY(faculty) REFERENCES faculties(id) ON DELETE SET NULL"]])
