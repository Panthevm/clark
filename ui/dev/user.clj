(ns user
  (:require [figwheel.main.api :as repl]))

(def figwheel-options
  {:id "app"
   :options {:main 'app.dev
             :output-to "resources/public/js/app.js"

             :output-dir "resources/public/js/out"}
   :config {:watch-dirs ["src"]
            :mode :serve
            :ring-server-options {:port 8081}}})

(comment

  (repl/start  figwheel-options)


  (repl/stop "app")
  (repl/cljs-repl))
