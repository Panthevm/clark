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
           :margin           "0px"
           :font-size        "15px"
           :font-family      "GothamPro"
           :height           "100%"}
    [:.faicon {:font-size "30px"}]
    [:.list-group-item {:cursor "pointer"}]
    [:.user-badge {:display         "flex"
                   :text-align      "center"
                   :text-decoration "none"}
     [:.username {:margin-right ".5rem"
                  :margin-top   ".25rem"}]]
    [:.sticky-table {:overflow-x "scroll"
                     :overflow-y "visible"}
     [:th {:border-top "none"}]
     [(keyword (str "th:nth-child(2)")) {:padding-left "145px"}]
     [:th :td {:white-space    "nowrap"
               :vertical-align "middle"}]
     [:.first-col {:padding-left "150px"}]
     [:.sticky-col {:position         "absolute"
                    :background-color "#ffffff"
                    :border-right     "1px solid black"
                    :height           "inherit"
                    :top              "auto"
                    :width            "140px"
                    :overflow-x       "hidden"
                    :text-overflow    "ellipsis"}]]
    [:.table {:margin-bottom "22px"}
     [:.form-control {:padding       "0"
                      :border-radius "0"
                      :min-width     "60px"
                      :font-size     "12px"}
      [:&:focus {:background "content-box"
                 :box-shadow "0 0 0 5px black"}]]
     [:input {:width      "100%"
              :border     "none"
              :background "content-box"
              :text-align "center"}]
     [:tr {:font-size "12px"}
      [:th :td {:vertical-align "middle"
                :border-right   "1px solid #d0d0d0"}]]
     [:.line {:height "43px"}]]
    [:.navbar {:border-radius "0 0 12px 12px"}]
    [:.bord {:border-radius "12px"}]
    [:.point {:cursor "pointer"}]
    [:.i-list
     [:i {:padding-left "10px"}]]
    [:.create {:background-color "#d5d5d5"}]
    [:.btn-form
     [:.btn {:background-color "#d5d5d5"
             :margin-top       "24px"
             :margin-right     "12px"}]]
    [:.delete-ico {:font-size     "22px"
                   :padding-right "4px"}]
    [:a :button:focus {:outline "none"}]
    [:.white {:background-color "#ffffff"}]
    [:.sidebar
     [:i {:width "25px"
          :color "#6c757d"}]
     [:span {:cursor  "pointer"
             :display "block"}]
     [:b {:display       "flex"
          :margin-bottom "7px"
          :border-bottom "1px solid #dee2e6"
          :color         "#6c757d"}]]
    [:.content-body {:margin-top "40px"}]
    [:.list-segment {:padding       "25px"
                     :margin-top    "26px"
                     :border-radius "25px"}]
    [:.segment {:padding          "25px"
                :background-color "#ffffff"
                :box-shadow       "0 .5rem 1rem rgba(0,0,0,.15)"
                :height           "max-content"
                :border-radius    "15px"}
     [:h2 {:font-size "25px" :font-family "GothamPro-medium"}]]
                                        ;Icon
    [:.spinner {:position            "absolute"
                :right               "12px"
                :z-index             "99"
                :align-self          "center"
                :color               "#6c757d"
                :width               "1rem"
                :height              "1rem"
                :border-right-color  "transparent"
                :border-radius       "50%"
                :border              ".25em solid currentColor"
                :border-top-width    "0.25em"
                :border-right-width  "0.25em"
                :border-bottom-width "0.25em"
                :border-left-width   "0.25em"
                :-webkit-animation   "spinner-border .75s linear infinite"
                :spinner-border      ".75s linear infinite"}]

                                        ;Form
    [:.combobox {:position "relative"}
     [:.items {:overflow-y    "auto"
               :margin-bottom "50px"
               :max-height    "286px"}]
     [:.menu {:border     "none"
              :margin-top "4px"
              :z-index    "99"
              :width      "100%"
              :position   "absolute"}]
     [:.icon {:padding       ".375rem .75rem"
              :align-items   "center"
              :margin-left   "1px"
              :border-radius ".25em 0 0 .25em"
              :display       "flex"}]
     [:.search {:border-radius ".25em .25em 0 0"}]
     [:.list-group-item:first-child
      {:border-radius "0"}]]
    [:.form {:padding "25px 0 25px 0"}
     [:label {:margin "8px 0px 0px 0px"}]
     [:.form-control {:padding ".5rem"}
      [:&:focus {:background-color "#e4e4e4"
                 :box-shadow       "none"}]]
     [:.input-group
      [:input :span
       {:background-color "#e4e4e4"
        :border           "none"}]]]]))
