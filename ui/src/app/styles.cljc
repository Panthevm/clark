(ns app.styles
  (:require [garden.core :as garden]))

(defn style [css]
  [:style (garden/css css)])
