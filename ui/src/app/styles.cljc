(ns app.styles
  (:require [garden.core :as garden]))

(defn style [css]
  [:style (garden/css css)])

(def form-button
  {:margin-right 12})

(def chip
  {:margin-right 4})

(def app-styles
  (style
   [:body {:margin "0px" :color "#333" :font-size "15px" :font-family "GothamPro" :height "100%"}
    [:.form {:margin "40px"}]]))
