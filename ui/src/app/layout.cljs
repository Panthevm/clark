(ns app.layout
  (:require [re-frame.core  :as    rf]
            [reagent.core   :as    r]
            [app.styles     :as    styles]
            [clojure.string :as    str]
            [app.helpers    :as    h]))

(defn current-nav [fragment navs]
  (map
   (fn [link]
     (if (str/includes? fragment (:href link))
       (assoc link :class "active font-weight-bold")
       link))
   navs))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment]
   (cond->> [{:href (h/href "schedule") :title "Журналы"}
             {:href (h/href "groups")   :title "Группы"}
             {:href (h/href "students") :title "Студенты"}]
     fragment (current-nav fragment))))

(defn navbar []
  (let [navs   (rf/subscribe [::navigation])
        user   (rf/subscribe [:xhr/response :user])
        expand (r/atom false)]
    (when @user
      (fn []
        (let [user (h/resource @user)]
          [:nav.navbar.navbar-expand-lg.navbar-light.white.shadow-sm
           [:div.container
            [:button.navbar-toggler {:on-click #(swap! expand not)}
             [:i.far.fa-bars]]
            [:div.navbar-collapse (when @expand {:class "collapse"})
             [:div.navbar-nav.mr-auto
              (map-indexed
               (fn [idx link] ^{:key idx}
                 [:a.nav-item.nav-link link
                  (:title link)])
               @navs)]
             [:a.user-badge {:href "#/profile"}
              [:span.username (h/remove-after (:username user) "@")]
              [:i.far.fa-user-circle.faicon]]]]])))))

(defn layout []
  (fn [context]
    [:div.app styles/app-styles
     [navbar]
     [:div.content-body context]]))
