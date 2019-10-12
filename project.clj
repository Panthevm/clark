(defproject app "0.1.0-SNAPSHOT"
  :description "Clark App"
  :dependencies [[org.clojure/clojure       "1.10.1"]
                 [org.clojure/java.jdbc     "0.7.10"]
                 [org.clojure/data.json     "0.2.6"]
                 [org.postgresql/postgresql "42.2.2"]
                 [http-kit                  "2.3.0"]
                 [ring/ring-json            "0.5.0"]
                 [ring-cors                 "0.1.13"]
                 [metosin/reitit-ring "0.3.10"]]
  :main app.dev)
