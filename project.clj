(defproject app "0.1.0-SNAPSHOT"
  :description "Clark App"
  :dependencies [[org.clojure/clojure       "1.10.1"]
                 [org.immutant/web          "2.1.10"]
                 [clj-pg                    "0.0.3"]
                 [ring/ring-json            "0.5.0"]
                 [metosin/reitit-ring       "0.3.10"]
                 [ring-cors                 "0.1.13"]]
  :main app.dev
  :uberjar-name "app.jar"
  :profiles {:uberjar {:aot [app.dev]}})
