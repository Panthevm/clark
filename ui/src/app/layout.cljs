(ns app.layout
  (:require [re-frame.core  :refer [reg-sub subscribe]]
            [reagent.core   :as    r]
            [app.styles     :refer [app-styles]]
            [clojure.string :refer [includes?]]
            [app.helpers    :as    h]))

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
   (cond->> [{:href (h/href "schedule") :title "Журналы"}
             {:href (h/href "groups")   :title "Группы"}
             {:href (h/href "students") :title "Студенты"}]
     fragment (current-nav fragment))))

(defn navbar [user]
  (let [navs   (subscribe [::navigation])
        user   (h/resource user)
        expand (r/atom false)]
    (fn []
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
          [:i.far.fa-user-circle.faicon]]]]])))

(defn layout []
  (let [user (subscribe [:xhr/response :user])]
    (fn [context]
      [:div.app app-styles
       (when user [navbar @user])
       [:div.content-body context]])))
