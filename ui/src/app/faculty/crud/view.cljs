(ns app.faculty.crud.view
  (:require [re-frame.core            :as rf]
            [app.faculty.crud.form    :as form]
            [app.faculty.crud.model   :as model]
            [reagent-material-ui.core :as ui]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.form.inputs          :as i]
            [app.uikit                :as kit]
            [app.pages                :as pages]
            [app.placeholders         :as ph]))

(defn form [id]
  [:div
   [i/input form/schema-path [:name] {:placeholder (:faculty-name ph/faculty)}]
   [:div.form-buttons
    [kit/ButtonSave #(rf/dispatch (if id [::model/update id] [::model/create]))]
    [kit/ButtonCancel #(rf/dispatch [::h/expand :dialog])]
    (when id
      [kit/ButtonDelete #(rf/dispatch [::model/delete id])])]])

(pages/reg-subs-page
 model/index-page
 (fn [_ params]
   [:div.container
    [form (:id params)]]))

