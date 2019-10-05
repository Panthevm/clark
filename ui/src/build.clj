(ns build
  (:require [cljs.build.api :as api]
            [clojure.string :as string]))

(def source-dir "src")

(def compiler-config
  {:output-to     "build/js/app.js"
   :source-map    "build/js/app.js.map"
   :output-dir    "build/js/out"
   :infer-externs true
   :optimizations :advanced
   :main 'app.prod
   :externs []})

(defn -main []
  (api/build source-dir compiler-config))
