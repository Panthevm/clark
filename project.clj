(defproject app "0.1.0-SNAPSHOT"
  :description "Clark App"
  :dependencies [[org.clojure/clojure       "1.10.1"]
                 [org.clojure/java.jdbc     "0.7.10"]
                 [org.clojure/data.json     "0.2.6"]
                 [honeysql                  "0.9.8"]
                 [org.postgresql/postgresql "42.2.2"]
                 [ring/ring-json            "0.5.0"]
                 [metosin/reitit-ring       "0.3.10"]
                 [http-kit                  "2.3.0"]
                 [ring-cors                 "0.1.13"]]
  :main app.dev)
