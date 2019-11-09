(ns app.students.view
  (:require [re-frame.core            :as rf]
            [app.students.model       :as model]
            [app.helpers              :refer [href]]
            [app.pages                :as pages]
            [app.students.crud.view]))

(defn Item [{{:keys [id name]} :resource}]
  [:a.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:redirect (href "students" id )])}
   [:span name]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [:div.container.segment.shadow.white
     [:h2 "Студенты"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord]]
      [:div.flex-shrink-1
       [:a {:href (href "students" "create")}
        [:button.btn.bord.shadow-sm.create.ml-1 "Создать"]]]]]
    [:div.container.list-segment.shadow.white
     [:div.list-group.list-group-flush
      (when (empty? items)
        [:a.list-group-item
         [:span "Нет данных"]])
      (map-indexed
       (fn [idx item] ^{:key idx} [Item item])
       items)]]]))