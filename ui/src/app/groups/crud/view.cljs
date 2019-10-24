(ns app.groups.crud.view
  (:require [re-frame.core            :as rf]
            [app.groups.crud.form     :as form]
            [app.groups.crud.model    :as model]
            [app.helpers              :as h]
            [app.form.inputs          :as i]
            [app.pages                :as pages]))
(defn Student
  [idx]
  [:div.form-row.col-sm
   [:div.flex-grow-1
    [i/input form/path [:students idx] {:placeholder "Фамилия Имя Отчество"}]]
   [:div.mt-2.ml-2
    [:text.text-danger.point
     {:on-click #(rf/dispatch [:zf/remove-collection-item form/path [:students] idx])}
     [:i.far.fa-times.delete-ico]]]])

(defn Form [{:keys [id idx-students]}]
  [:div
   [:div.row
    [:div.col-sm.segment.shadow.white.form
     [:h2 "Основная информация"]
     [:div.row>div.col-sm
      [i/input form/path [:name]       {:label "Название"}]]
     [:div.row>div.col-sm
      [i/input form/path [:faculty]    {:label "Факультет"}]]
     [:div.row>div.col-sm
      [i/input form/path [:department] {:label "Факультет"}]]
     [:div.row>div.col-sm-3
      [i/input form/path [:course]     {:label "Курс"}]]
     [:div.flex-row.btn-form
      [:button.btn
       {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
       "Сохранить"]
      [:button.btn
       {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri "#/groups"}])}
       "Отменить"]
      (when id
        [:button.btn
         {:on-click #(rf/dispatch [::model/delete id])}
         "Удалить"])]]
    [:div.col-sm.segment.shadow.white.ml-md-3.mt-3.mt-md-0.form.form
     [:h2 "Студенты"]
     (for [idx idx-students] ^{:key idx}
       [Student idx])
     [:text.text-primary.point
      {:on-click #(rf/dispatch [:zf/add-collection-item form/path [:students] ""])}
      "Добавить"]]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [idx-students]} {id :id}]
   [:div.container
    [Form {:idx-students idx-students
           :id id}]]))

(pages/reg-subs-page
 model/create-page
 (fn [{:keys [idx-students]}]
   [:div.container
    [Form {:idx-students idx-students}]]))
