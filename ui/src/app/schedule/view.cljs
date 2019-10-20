(ns app.schedule.view
  (:require [re-frame.core            :as rf]
            [app.schedule.model         :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]
            [app.schedule.crud.view]))

(defn Item [{{:keys [id group]} :resource}]
  [:button.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (href "schedule" id)}])}
   [:span group]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [:div.container.segment.shadow.white
     [:h2 "Журналы"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord]]
      [:div.flex-shrink-1
       [:button.btn.bord.shadow-sm
        {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (href "schedule" "create")}])}
        "Создать"]]]]
    [:div.container.list-segment.shadow.white
     [:div.list-group.list-group-flush
      (when (empty? items)
        [:a.list-group-item
         [:span "Нет данных"]])
      (for [item items] ^{:key (:id item)}
        [Item item])]]]))

