(ns app.layout
  (:require [re-frame.core            :as rf]
            [app.styles               :as styles]
            [app.helpers              :as helpers]))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment _]
   [{:id "schedule" :href (helpers/href "schedule")  :display "Журналы"}
    {:id "groups"   :href (helpers/href "groups")    :display "Группы"}]))

(defn navbar []
  (let [menu (rf/subscribe [::navigation])]
    (fn []
      [:nav.navbar.navbar-expand-lg.navbar-light.white.shadow-sm
       [:button.navbar-toggler
        [:i.fas.fa-bars]]
       [:div.navbar-collapse
        [:div.navbar-nav
         (for [{:keys [id href display]} @menu]
           [:a.nav-item.nav-link {:key id :href href}
            display])]]])))

(defn layout []
  (fn [cnt]
    [:div.app styles/app-styles
     [navbar]
     [:div.content-body cnt]]))
