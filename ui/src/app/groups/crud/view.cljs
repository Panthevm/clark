(ns app.groups.crud.view
  (:require [re-frame.core            :as rf]
            [app.groups.crud.form     :as form]
            [app.groups.crud.model    :as model]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.form.inputs          :as i]
            [app.pages                :as pages]
            [app.placeholders         :as ph]))

(defn form [id]
  #_[:div.form
   [i/input form/schema-path [:department]      {:placeholder ph/groups}] [:br]
   [i/input form/schema-path [:course]          {:placeholder ph/groups}] [:br]
   [i/input form/schema-path [:students_number] {:placeholder ph/groups}] [:br]
   [:div.form-buttons
    [kit/ButtonSave #(rf/dispatch (if id [::model/update id] [::model/create]))]
    [kit/ButtonCancel #(rf/dispatch [:zframes.redirect/redirect {:uri "#/groups"}])]
    (when id
      [kit/ButtonDelete #(rf/dispatch [::model/delete id])])]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [item]} params]
   [:div.container
    [form (:id params)]]))

