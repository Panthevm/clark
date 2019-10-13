(ns app.layout
  (:require [reagent-material-ui.core :as ui]
            [app.uikit                :as kit]
            [re-frame.core            :as rf]
            [reagent.core             :as r]
            [app.styles               :as styles]
            [app.helpers              :as helpers]
            [clojure.string           :as str]))

(def el r/as-element)

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
   (current-page [{:id "groups"    :href (helpers/href "groups")    :display "Группы"           :ico "/img/meeting_room-24px.svg"}]
                 fragment)))


(defn navbar []
  (let [menu (rf/subscribe [::navigation])
        expand #(rf/dispatch [::helpers/expand :nav])]
    (fn []
      (let [is-open? (helpers/expand? :nav)]
        [:div
         [ui/AppBar {:iconElementRight (el [ui/FlatButton {:label "ВОЙТИ"}])
                     :onLeftIconButtonTouchTap #(expand)}]
         [ui/Drawer {:open is-open?
                     :docked false
                     :onRequestChange #(expand)}
          [ui/List
           [ui/Subheader "Меню"]
           [ui/Divider]
           (for [i @menu]
             [ui/ListItem {:key (:href i)
                           :rightIcon (el [ui/Avatar {:src (:ico i)
                                                      :backgroundColor "none"}])
                           :isKeyboardFocused (:active i)
                           :on-click (fn []
                                       (rf/dispatch [:zframes.redirect/redirect {:uri (:href i)}])
                                       (expand))}
              (:display i)])]]]))))

(defn Snack []
  (let [flash (rf/subscribe [:page/data :flash])]
    (fn []
      (when (not (empty? @flash))
        [ui/Snackbar {:open true
                      :autoHideDuration 3000
                      :message (:msg @flash)}]))))

(defn layout []
  (fn [cnt]
    [:div.app styles/app-styles
     [ui/MuiThemeProvider kit/theme-defaults
      [:div
       [navbar]
       [:div cnt]
       [Snack]]]]))
