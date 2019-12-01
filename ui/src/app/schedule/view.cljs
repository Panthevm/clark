(ns app.schedule.view
  (:require [re-frame.core            :as rf]
            [app.schedule.model         :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]

            [app.schedule.crud.view]
            [app.schedule.show.view]
            [app.schedule.report.view]))

(defn Item [{{id :id {name :name} :group discipline :discipline} :resource}]
  [:a.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:redirect (href "schedule" id "show")])}
   [:span [:b.pr-2 discipline]  name]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div.container
    [:div.segment
     [:h2 "Журналы"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord]]
      [:div.flex-shrink-1
       [:button.btn.create.ml-1
        {:on-click #(rf/dispatch [:redirect (href "schedule" "create")])}
        "Создать"]]]]
    [:div.segment
     [:div.list-group.list-group-flush
      (if (empty? items)
        [:a.list-group-item [:span "Нет данных"]]
        (map-indexed
         (fn [idx item] ^{:key idx}
           [Item item])
         items))]]]))
