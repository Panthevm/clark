(ns ^:figwheel-no-load app.dev
  (:require [app.core :as core]
            [re-frisk.core :refer [enable-re-frisk!]]
            [devtools.core :as devtools]))

(devtools/install!)
(enable-console-print!)
(enable-re-frisk! {:width "400px" :height "500px"})
(core/mount-root)

(defn ^:after-load re-render [] (core/mount-root))
