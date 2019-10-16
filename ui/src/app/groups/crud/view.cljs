(ns app.groups.crud.view
  (:require [re-frame.core            :as rf]
            [app.groups.crud.form     :as form]
            [app.groups.crud.model    :as model]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.form.inputs          :as i]
            [app.pages                :as pages]))

(defn Form [& [id]]
  [:div
   [:div..form
    [i/input form/schema-path [:department] {:label "Кафедра"}]
    [:div.row>div.col-2
     [i/input form/schema-path [:course]     {:label "Курс"}]]
    [:table.table.table-sm
     [:thead
      [:th.numeric "№"]
      [:th "ФИО"]]
     [:tbody
      [:tr
       [:th "1"]
       [:th "Багров Иван Владимирович"]]]]]
   [:div.row
    [:button.btn
     {:on-click #(rf/dispatch (if id [::model/update id] [::model/fcreate]))}
     "Сохранить"]
    [:button.btn
     {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri "#/groups"}])}
     "Отменить"]
    [:button.btn
     {:on-click #(rf/dispatch [::model/delete id])}
     "Удалить"]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [item]} params]
   [:div.container.segment.shadow.white
    [:h2 "Редактирование группы"]
    [Form (:id params)]]))

(pages/reg-subs-page
 model/create-page
 (fn [{:keys [item]} params]
   [:div.container.segment.shadow.white
    [:h2 "Создание группы"]
    [Form]]))
