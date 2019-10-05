(ns app.styles
  (:require [garden.core :as garden]))

(defn style [css]
  [:style (garden/css css)])

(def form-button
  {:margin-right 12})

(def chip
  {:margin-right 4})
 
