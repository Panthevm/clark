(ns app.styles
  (:require [garden.core :as garden]))

(defn style [css]
  [:style (garden/css css)])

(def app-styles
  (style
   [:body {:background-color "#e4e4e4"
           :margin "0px"
           :font-size "15px"
           :font-family "GothamPro"
           :height "100%"}
    [:.table
     [:.numeric
      {:width "20px"}]]
    [:.navbar {:border-radius "0 0 12px 12px"}]
    [:.bord {:border-radius "12px"}]
    [:.btn {:background-color "#d5d5d5"}]
    [:.white {:background-color "#ffffff"}]
    [:.content-body {:margin-top "40px"}]
    [:.form-row {:padding "5px"}]
    [:.form {:padding "25px 0 25px 0"}
     [:.form-control
      [:&:focus {:background-color  "#e4e4e4"
                 :box-shadow "none"}]]
     [:input {:background-color  "#e4e4e4"
              :border           "none"}]]
    [:.list-segment {:padding "25px"
                     :margin-top "26px"
                     :border-radius "25px"}]
    [:.segment {:padding "25px"
                :border-radius "25px"}
     [:h2 {:font-size "25px" :font-family "GothamPro-medium"}]
     [:.btn {:margin-left "12px"}]]]))
