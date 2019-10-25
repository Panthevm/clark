(ns app.schedule.show.view
  (:require [re-frame.core           :as rf]
            [app.schedule.crud.form  :as form]
            [app.schedule.show.model :as model]
            [app.helpers             :as h]
            [app.pages               :as pages]))

(defn Line
  [people]
  [:tr.line
   [:td.sticky-col (h/short-name people)]
   [:td.first-col [:input]]
   [:td [:input]]
   [:td "2"]
   [:td ""]
   [:td "Н"]
   [:td "5"]
   [:td "Н"]
   [:td ""]
   [:td ""]
   [:td "2"]
   [:td "5"]
   [:td "Н"]
   [:td ""]
   [:td "Н"]
   [:td ""]
   [:td "2"]
   [:td "5"]
   [:td ""]
   [:td "5"]
   [:td ""]
   [:td "Н"]
   [:td ""]
   [:td "2"]
   [:td ""]
   [:td ""]
   [:td "5"]
   [:td "Н"]
   [:td "5"]])

(defn Table
  [group days]
  [:div.sticky-table
   [:table.table.table-striped
      [:thead
       [:tr
        [:th.sticky-col "Студент"]
        (for [day days]
          [:th {:key day} day])]]
      [:tbody
       (map-indexed
        (fn [idx people] ^{:key idx}
          [Line people idx])
        (:students group))]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [group shedule days]} {id :id}]
   [:div.container.segment.shadow.white
    [:div.d-flex.justify-content-between
     [:h2 "Журнал группы " (:name group)]
     [:i.far.fa-edit.point
      {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (h/href "schedule" id)}])}]]
    [Table group days]
    [:div.btn-form
     [:button.btn
      {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
      "Сохранить"]]]))
