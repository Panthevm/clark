(ns app.groups.crud.view
  (:require [re-frame.core            :as rf]
            [app.groups.crud.form     :as form]
            [app.groups.crud.model    :as model]
            [app.helpers              :as h]
            [app.form.inputs          :as i]
            [app.pages                :as pages]))
(defn Student
  [idx]
  [:div.form-row
   [:div.flex-grow-1
    (prn idx)
    [i/input form/path [:students idx] {:placeholder "Фамилия Имя Отчество"}]]
   [:div.mt-2.ml-2
    [:span.text-danger
     {:on-click #(rf/dispatch [:zf/remove-collection-item form/path [:students] idx])}
     "Удалить"]]])

(defn Form [{:keys [id idx-students]}]
  [:div
   [:div.form
    [i/input form/path [:department] {:label "Кафедра"}]
    [:div.row>div.col-2
     [i/input form/path [:course]     {:label "Курс"}]]
    [:div
     [:text.pr-2 "Студенты"]
     (for [idx idx-students] ^{:key idx}
       [Student idx])
     [:span.text-primary.pointer
      {:on-click #(rf/dispatch [:zf/add-collection-item form/path [:students] ""])}
      "Добавить"]]]

   [:div.row
    [:button.btn
     {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
     "Сохранить"]
    [:button.btn
     {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri "#/groups"}])}
     "Отменить"]
    (when id 
      [:button.btn
       {:on-click #(rf/dispatch [::model/delete id])}
       "Удалить"])]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [idx-students]} {id :id}]
   [:div.container.segment.shadow.white
    [:h2 "Редактирование группы"]
    [Form {:idx-students idx-students
           :id id}]]))

(pages/reg-subs-page
 model/create-page
 (fn [{:keys [idx-students]}]
   [:div.container.segment.shadow.white
    [:h2 "Создание группы"]
    [Form {:idx-students idx-students}]]))
