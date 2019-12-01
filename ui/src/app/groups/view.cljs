(ns app.groups.view
  (:require [re-frame.core            :as rf]
            [app.groups.model         :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]
            [app.groups.crud.view]))

(defn Item [{{:keys [id name]} :resource}]
  [:a.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:redirect (href "groups" id )])}
   [:span name]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div.container
    [:div.segment
     [:h2 "Группы"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord]]
      [:div.flex-shrink-1
       [:a {:href (href "groups" "create")}
        [:button.btn.create.ml-1 "Создать"]]]]]
    [:div.segment
     [:div.list-group.list-group-flush
      (when (empty? items)
        [:a.list-group-item [:span "Нет данных"]])
      (map-indexed
       (fn [idx item] ^{:key idx} [Item item])
       items)]]]))
