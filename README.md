# Clark

Keeping track of student performance

## Dependencies

Backend: 
```clj
[org.clojure/java.jdbc     "0.7.10"]
[org.postgresql/postgresql "42.2.2"]
[http-kit                  "2.3.0"]
[ring/ring-json            "0.5.0"]
[ring-cors                 "0.1.13"]
[metosin/reitit-ring "0.3.10"]
```
Frontend:

```clj
reagent                   {:mvn/version "0.7.0"}
re-frame                  {:mvn/version "0.10.6"}
reagent-utils             {:mvn/version "0.3.1"}
cljs-http                 {:mvn/version "0.1.45"}
hiccup                    {:mvn/version "1.0.5"}
garden                    {:mvn/version "1.3.5"}
route-map                 {:mvn/version "0.0.7-RC1"}
reagent-material-ui       {:mvn/version "0.2.5"}
zenform                   {:local/root "zenform"}
chrono                    {:local/root "chrono"}}
```
