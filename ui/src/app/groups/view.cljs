(ns app.groups.view
  (:require [re-frame.core            :as rf]
            [app.groups.model         :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]
            [app.groups.crud.view]))

(defn Item [{{:keys [id department]} :resource}]
  [:a.list-group-item.list-group-item-action {:href (href "groups" id)}
   [:span department]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [:div.container.segment.shadow.white
     [:h2 "Группы"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord]]
      [:div.flex-shrink-1
       [:a {:href (href "groups" "create")}
        [:button.btn.bord.shadow-sm "Создать"]]]]]
    [:div.container.list-segment.shadow.white
     [:div.list-group.list-group-flush
      (when (empty? items)
        [:a.list-group-item
         [:span "Нет данных"]])
      (for [item items] ^{:key (:id item)}
        [Item item])]]]))
