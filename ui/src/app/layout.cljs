(ns app.layout
  (:require [re-frame.core            :as rf]
            [app.styles               :refer [app-styles]]
            [clojure.string           :refer [includes?]]
            [app.helpers              :refer [href]]))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment]
   (->>
    [{:href (href "schedule")  :display "Журналы"}
     {:href (href "groups")    :display "Группы"}]
    (map
     (fn [link]
       (if (includes? (:href link) fragment)
         (assoc link :class "active font-weight-bold")
         link))))))

(defn navbar [menu]
  [:nav.navbar.navbar-expand-lg.navbar-light.white.shadow-sm
   [:div.container
    [:button.navbar-toggler
     [:i.far.fa-bars]]
    [:div.navbar-collapse
     [:div.navbar-nav
      (map-indexed
       (fn [idx {:keys [href display class]}]
         [:a.nav-item.nav-link {:key idx :href href :class class}
          display])
       menu)]]]])

(defn layout []
  (let [menu (rf/subscribe [::navigation])]
    (fn [cnt]
      [:div.app app-styles
       [navbar @menu]
       [:div.content-body cnt]])))
