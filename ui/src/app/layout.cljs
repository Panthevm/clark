(ns app.layout
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [app.styles :as styles]
            [zframes.flash :as flashes]
            [zframes.modal :as modal]
            [app.helpers :as helpers]
            [clojure.string :as str]))

(defn current-page
  [navs fragment]
  (->> navs
       (map
        #(if (re-find (re-pattern (:href %)) (str fragment))
           (assoc % :class "active font-weight-bold")
          %))))

(rf/reg-sub
 ::navigation
 :<- [:route-map/fragment]
 (fn [fragment _]
   (current-page [{:id "main" :href (helpers/href "/") :display "Главная страница"}
                  {:id "post" :href (helpers/href "post") :display "Сборы"}]
                 fragment)))

(def app-styles
  (styles/style
   [:body {:color "#333" :font-size "15px" :font-family "GothamPro" :height "100%"}]))

(defn navbar []
  (let [navigation* (rf/subscribe [::navigation])]
    (fn []
      (let [menu @navigation*]
        [:nav.navbar.navbar-expand-lg.navbar-light.bg-light.sticky-top
         [:div.container.pl-3.pr-3
          [:div.navbar-collapse
           [:div.navbar-nav.grow-1
            (for [i menu]
              [:a.nav-item.nav-link
               {:class (:class i)
                :key (:href i)
                :href (:href i)}
               (:display i)])]]]]))))

(defn layout []
  (fn [cnt]
    [:div.app app-styles
     [navbar]
     [:div cnt]

     flashes/styles

     [modal/modal]
     [flashes/flashes]]))
