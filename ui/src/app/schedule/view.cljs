(ns app.schedule.view
  (:require [re-frame.core            :as rf]
            [app.schedule.model         :as model]
            [app.placeholders         :as ph]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.helpers              :as h]
            [app.pages                :as pages]))

(pages/reg-subs-page
 model/index-page
 (fn [sidebar]
   [:div
    (when (h/expand? :sidebar)
      [:div.ui.visible.inverted.left.uncover.vertical.sidebar.menu {:style {:background "white"}}
       [:a.item "."]
       [:div.item
        [:div.ui.form
         [:div.ui.header "Журналы"
          [:button.mini.ui.button.right.floated "Добавить"]]

         [:div.field
          [:input {:placeholder "Поиск"}]]]]])
    [:div.pusher
     [:div.ui.very.basic.collapsing.celled.table
      [:thead
       [:tr
        [:th "Студент"]
        [:th "0.1"]]]
      [:tbody
       [:tr
        [:td
         [:h4.ui.header
          [:div.content "Багров"
           [:div.sub.header "Иван Владимирович"]]]]
        [:td "2"]]]]]]))

