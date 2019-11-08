(ns app.students.crud.view
  (:require [re-frame.core            :as rf]
            [app.students.crud.form     :as form]
            [app.students.crud.model    :as model]
            [app.helpers              :as h]
            [app.form.inputs          :as i]
            [app.pages                :as pages]))

(defn Form [{:keys [id idx-students]}]
  [:div
   [:div.row
    [:div.col-sm.segment.shadow.white.form
     [:h2 "Основная информация"]
     [:div.row>div.col-sm
      [:label "ФИО"]
      [i/input form/path [:name]]]
     [:div.flex-row.btn-form
      [:button.btn
       {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
       "Сохранить"]
      [:button.btn
       {:on-click #(rf/dispatch [:redirect "#/students"])}
       "Отменить"]
      (when id
        [:button.btn
         {:on-click #(rf/dispatch [::model/delete id])}
         "Удалить"])]]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [idx-students]} {id :id}]
   [:div.container
    [Form {:id id}]]))

(pages/reg-subs-page
 model/create-page
 (fn [{:keys [idx-students]}]
   [:div.container
    [Form]]))
