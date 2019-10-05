(ns app.layout
  (:require [reagent-material-ui.core :as ui]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [app.styles :as styles]
            [zframes.flash :as flashes]
            [zframes.modal :as modal]
            [app.helpers :as helpers]
            [clojure.string :as str]))
(def el r/as-element)
(defn color [nme] (aget ui/colors nme))
(defn icon [nme] [ui/FontIcon {:className "material-icons"} nme])

;; create a new theme based on the dark theme from Material UI
(defonce theme-defaults {:muiTheme (ui/getMuiTheme
                                    (-> ui/lightBaseTheme
                                        (js->clj :keywordize-keys true)
                                        (update :palette merge {:primary1Color (color "cyan500")
                                                                :primary2Color (color "cyan500")})
                                        clj->js))})
(defn current-page
  [navs fragment]
  (->> navs
       (map
        #(if (re-find (re-pattern (:href %)) (str fragment))
           (assoc % :active true)
          %))))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment _]
   (current-page [{:id "main" :href (helpers/href "/") :display "Главная страница" :ico  "/img/home-24px.svg"}
                  {:id "post" :href (helpers/href "post") :display "Аудитории" :ico "/img/meeting_room-24px.svg"}]
                 fragment)))

(def app-styles
  (styles/style
   [:body {:color "#333" :font-size "15px" :font-family "GothamPro" :height "100%"}]))

(defn navbar []
  (let [navigation* (rf/subscribe [::navigation])
        is-open? (r/atom false)
        expand #(swap! is-open? not)]
    (fn []
      (let [menu @navigation*]
        [ui/MuiThemeProvider theme-defaults
         [:div
          [ui/AppBar {:title "CLARK"
                      :iconElementRight (el [ui/FlatButton {:label "ВОЙТИ"}])

                      :onLeftIconButtonTouchTap #(expand)}]
          [ui/Drawer {:open @is-open?
                      :docked false
                      :onRequestChange #(expand)}
           [ui/List
            [ui/Subheader "Меню"]
            [ui/Divider]
              (for [i menu]
                [ui/ListItem {:key (:href i)
                              :hoverColor (color "cyan500")
                              :rightIcon (el [ui/IconMenu {:iconButtonElement (el [:img {:src (:ico i)}])}])
                              :isKeyboardFocused (:active i)
                              :on-click (fn []
                                          (rf/dispatch [:zframes.redirect/redirect {:uri (:href i)}])
                                          (expand))}
                 (:display i)])]]]]))))

(defn layout []
  (fn [cnt]
    [:div.app app-styles
     [navbar]
     [:div cnt]

     flashes/styles

     [modal/modal]
     [flashes/flashes]]))
