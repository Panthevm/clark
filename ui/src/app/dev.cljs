(ns ^:figwheel-no-load app.dev
  (:require [app.core :as core]
            [re-frisk.core :refer [enable-re-frisk!]]))

(enable-console-print!)
(enable-re-frisk! {:width "400px" :height "500px"})
(core/mount-root)
