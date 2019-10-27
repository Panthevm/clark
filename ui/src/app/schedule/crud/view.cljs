(ns app.schedule.crud.view
  (:require [re-frame.core            :as rf]
            [app.schedule.crud.form     :as form]
            [app.schedule.crud.model    :as model]
            [app.helpers              :as h]
            [app.form.inputs          :as i]
            [app.pages                :as pages]))

(defn Form [{:keys [id]}]
  [:div
   [:div.form
    [:label "Группа"]
    [i/combobox form/path [:group]]
    [:label "Дисциплина"]
    [i/input    form/path [:discipline]]]

   [:div.flex-row.btn-form
    [:button.btn
     {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
     "Сохранить"]
    [:button.btn
     {:on-click #(rf/dispatch [::model/main-redirect {:route id}])}
     "Отменить"]
    (when id
      [:button.btn
       {:on-click #(rf/dispatch [::model/delete id])}
       "Удалить"])]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [idx-students]} {id :id}]
   [:div.container.segment.shadow.white
    [:h2 "Редактирование журнала"]
    [Form {:id id}]]))

(pages/reg-subs-page
 model/create-page
 (fn [{:keys [idx-students]}]
   [:div.container.segment.shadow.white
    [:h2 "Создание журнала"]
    [Form]]))

