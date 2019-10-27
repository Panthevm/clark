(ns app.schedule.show.view
  (:require [re-frame.core           :as rf]
            [app.form.inputs         :as i]
            [app.schedule.crud.form  :as form]
            [app.schedule.crud.model :as crud]
            [app.schedule.show.model :as model]
            [app.helpers             :as h]
            [app.pages               :as pages]))

(defn Line
  [people idx-days idxs]
  [:tr.line
   [:td.sticky-col (h/short-name people)]
   (for [idx idx-days]
     [:th [i/input form/path [:schedule idx :assessment idxs]]])])

(defn Table
  [group idx-days]
  [:div.sticky-table
   [:table.table.table-striped.table-sm
    [:thead
     [:tr
      [:th.sticky-col.line]
      (for [idx idx-days]
        [:th [i/input form/path [:schedule idx :date]]])]]
    [:tbody
     (map-indexed
      (fn [idx people] ^{:key idx}
        [Line people idx-days idx])
      (:students group))]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [group shedule idx-days]} {id :id}]
   [:div.container.segment.shadow.white
    [:div.d-flex.justify-content-between
     [:h2 "Журнал группы " (:name group)]
     [:div.i-list
      [:i.far.fa-edit.point
       {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (h/href "schedule" id)}])}]
      [:i.far.fa-plus.point
       {:on-click #(rf/dispatch [::model/add-column])}]]]
    [Table group idx-days]
    [:div.btn-form
     [:button.btn
      {:on-click #(rf/dispatch [::crud/update id])}
      "Сохранить"]]]))
