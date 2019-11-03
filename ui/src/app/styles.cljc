(ns app.styles
  (:require
   [garden.stylesheet :as stylesheet]
   [garden.core :as garden]))

(defn rule [tag attrs]
  ((stylesheet/rule tag) attrs))

(defn style [css]
  [:style (garden/css css)])

(def app-styles
  (style
   [:body {:background-color "#e4e4e4"
           :margin "0px"
           :font-size "15px"
           :font-family "GothamPro"
           :height "100%"}
    [:.sticky-table {:overflow-x "scroll"
                     :overflow-y "visible"}
     [:th {:border-top "none"}]
     [(keyword (str "th:nth-child(2)")) {:padding-left "145px"}]
     [:th :td {:white-space "nowrap"
               :vertical-align "middle"}]
     [:.first-col {:padding-left "150px"}]
     [:.sticky-col {:position "absolute"
                    :background-color "#ffffff"
                    :border-right "1px solid black"
                    :height "inherit"
                    :top "auto"
                    :width "140px"
                    :overflow-x "hidden"
                    :text-overflow "ellipsis"}]]
    [:.table {:margin-bottom "0px"}
     [:.form-control {:padding "0"
                      :border-radius "0"
                      :min-width "60px"
                      :font-size "12px"}
      [:&:focus {:background "content-box"
                 :box-shadow "0 0 0 5px black"}]]
     [:input {:width "100%"
              :border "none"
              :background "content-box"
              :text-align "center"}]
     [:tr {:font-size "12px"}
      [:th :td {:vertical-align "middle"
                :border-right "1px solid #d0d0d0"}]]
     [:.line {:height "43px"}]]
    [:.navbar {:border-radius "0 0 12px 12px"}]
    [:.bord {:border-radius "12px"}]
    [:.point {:cursor "pointer"}]
    [:.i-list
     [:i {:padding-left "10px"}]]
    [:.create {:background-color "#d5d5d5"}]
    [:.btn-form
     [:.btn {:background-color "#d5d5d5"
             :margin-top "24px"
             :margin-right "12px"}]]
    [:.delete-ico {:font-size "22px"
                   :padding-right "4px"}]
    [:a :button:focus {:outline "none"}]
    [:.white {:background-color "#ffffff"}]
    [:.content-body {:margin-top "40px"}]
    [:.form-row {:padding "5px"}]
    [:.combobox {:position "relative"}
     [:.menu {:border "none"
              :z-index "99"
              :width "100%"
              :position "absolute"}]
     [:.icon {:padding ".375rem .75rem"
              :align-items "center"
              :margin-left "1px"
              :border-radius ".25em 0 0 .25em"

              :display "flex"}]
     [:.search {:border-radius ".25em .25em 0 0"}]
     [:.list-group-item:first-child
      {:border-radius "0"}]]
    [:.form {:padding "25px 0 25px 0"}
     [:.form-control {:padding ".5rem"}
      [:&:focus {:background-color  "#e4e4e4"
                 :box-shadow "none"}]]
     [:input :span
      {:background-color  "#e4e4e4"
       :border           "none"}]]
    [:.list-segment {:padding "25px"
                     :margin-top "26px"
                     :border-radius "25px"}]
    [:.segment {:padding "25px"
                :height "max-content"
                :min-width "500px"
                :border-radius "25px"}
     [:h2 {:font-size "25px" :font-family "GothamPro-medium"}]]]))
