(ns app.students.view
  (:require [re-frame.core            :as rf]
            [app.students.model       :as model]
            [app.helpers              :as h]
            [app.pages                :as pages]
            [app.students.crud.view]))

(defn Item [{{:keys [id name]} :resource}]
  [:a.list-group-item.list-group-item-action
   {:on-click #(rf/dispatch [:redirect (h/href "students" id )])}
   [:span name]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div.container
    [:div.segment
     [:h2 "Студенты"]
     [:div.form-row
      [:div.flex-grow-1
       [:input.form-control.bord {:on-change h/search}]]
      [:a {:href (h/href "students" "create")}
       [:button.btn.create.ml-1 "Создать"]]]]
    [:div.segment
     [:div.list-group.list-group-flush
      (if (empty? items)
        [:a.list-group-item
         [:span "Нет данных"]]
        (map-indexed
         (fn [idx item] ^{:key idx} [Item item])
         items))]]]))
