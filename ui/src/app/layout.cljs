(ns app.layout
  (:require [re-frame.core            :as rf]
            [app.styles               :as styles]
            [app.helpers              :as helpers]))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment _]
   [{:id "schedule" :href (helpers/href "schedule")  :display "Расписания"}
    {:id "groups"   :href (helpers/href "groups")    :display "Группы"}]))

(defn navbar []
  (let [menu (rf/subscribe [::navigation])]
    (fn []
      [:div.ui.top.fixed.menu
       [:a.item {:on-click #(rf/dispatch [::helpers/expand :sidebar])} "Меню"]
       (for [{:keys [id href display]} @menu]
         [:a.item {:key id :href href}
          display])])))

(defn layout []
  (fn [cnt]
    [:div.app styles/app-styles
     [:div
      [navbar]
      [:div.content-body cnt]]]))
