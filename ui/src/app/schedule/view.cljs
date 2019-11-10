(ns app.schedule.view
  (:require [re-frame.core            :as rf]
            [app.schedule.model         :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]

            [app.schedule.crud.view]
            [app.schedule.report.view]))

(defn Item [{{id :id {name :name} :group} :resource}]
  [:a.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:redirect (href "schedule" id "show")])}
   [:span name]])

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
       [:button.btn.bord.shadow-sm.create.ml-1
        {:on-click #(rf/dispatch [:redirect (href "schedule" "create")])}
        "Создать"]]]]
    [:div.container.list-segment.shadow.white
     [:div.list-group.list-group-flush
      (when (empty? items)
        [:a.list-group-item
         [:span "Нет данных"]])
      (map-indexed 
       (fn [idx item] ^{:key idx}
         [Item item])
       items)]]]))

