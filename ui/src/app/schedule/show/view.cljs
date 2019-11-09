(ns app.schedule.show.view
  (:require [re-frame.core           :as rf]
            [app.form.inputs         :as i]
            [app.schedule.crud.form  :as form]
            [app.schedule.crud.model :as crud]
            [app.schedule.show.model :as model]
            [app.helpers             :as h]
            [app.pages               :as pages]))

(defn Line
  [student row {:keys [idx-days idx-rows]}]
  [:tr.line.d-flex.align-items-start
   [:td.sticky-col (h/short-name (:display student))]
   (for [col idx-days] ^{:key col}
     [:th [i/merge-input form/path [:schedule col :assessment row :grade] {:subject student
                                                                 :number row}]])])

(defn Table
  [{:keys [group idx-days] :as page}]
  [:div.sticky-table
   [:table.table.table-striped.table-sm
    [:thead
     [:tr.d-flex.align-items-start
      [:th.sticky-col.line]
      (for [idx idx-days] ^{:key idx}
        [:th {:on-blur  (fn [] (js/setTimeout #(rf/dispatch [::model/remove-select]) 400))
              :on-click #(rf/dispatch [::model/select-column idx])}
         [i/input form/path [:schedule idx :date]]])]]
    [:tbody
     (map-indexed
      (fn [idx-row student] ^{:key idx-row}
        [Line student idx-row page])
      (:students group))]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [group selected-colum] :as page} {id :id}]
   [:div.container.segment.shadow.white
    [:div.d-flex.justify-content-between
     [:h2 "Журнал группы " (:name group)]
     [:div.i-list
      [:i.far.fa-edit.point
       {:on-click #(rf/dispatch [:redirect (h/href "schedule" id)])}]
      [:i.far.fa-plus.point
       {:on-click #(rf/dispatch [::model/add-column])}]
      (when selected-colum
        [:i.far.fa-times.point
         {:on-click #(rf/dispatch [::model/remove-column selected-colum])}])]]
    [Table page]
    [:div.btn-form
     [:button.btn
      {:on-click #(rf/dispatch [::crud/update id])}
      "Сохранить"]]]))
