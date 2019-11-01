(ns app.layout
  (:require [re-frame.core  :refer [reg-sub subscribe]]
            [reagent.core   :as    r]
            [app.styles     :refer [app-styles]]
            [clojure.string :refer [includes?]]
            [app.helpers    :refer [href]]))

(defn current-nav [fragment navs]
  (map
   (fn [link]
     (if (includes? fragment (:href link))
       (assoc link :class "active font-weight-bold")
       link))
   navs))

(reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment]
   (cond->>
       [{:href (href "schedule") :title "Журналы"}
        {:href (href "groups")   :title "Группы"}]
     fragment (current-nav fragment))))

(defn navbar []
  (let [navs (subscribe [::navigation])
        expand (r/atom false)]
    (fn []
      [:nav.navbar.navbar-expand-lg.navbar-light.white.shadow-sm
       [:div.container
        [:button.navbar-toggler {:on-click #(swap! expand not)}
         [:i.far.fa-bars]]
        [:div.navbar-collapse (when @expand {:class "collapse"})
         [:div.navbar-nav
          (map-indexed
           (fn [idx link] ^{:key idx}
             [:a.nav-item.nav-link link
              (:title link)])
           @navs)]]]])))

(defn layout [cnt]
  [:div.app app-styles
   [navbar]
   [:div.content-body cnt]])
