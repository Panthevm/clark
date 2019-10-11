(ns app.faculty.crud.view
  (:require [re-frame.core            :as rf]
            [app.faculty.crud.form     :as form]
            [app.faculty.crud.model    :as model]
            [reagent-material-ui.core :as ui]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.form.inputs          :as i]
            [app.pages                :as pages]
            [app.placeholders         :as ph]))

(defn form [id]
  [:div
   [i/input form/schema-path [:name] {:placeholder (:faculty-name ph/faculty)}]
   [:div.form-buttons
    [ui/RaisedButton {:label (:save ph/button)
                      :on-click #(rf/dispatch (if id [::model/update id] [::model/create]))
                      :style s/form-button
                      :primary true}]
    [ui/RaisedButton {:label (:cancel ph/button)
                      :style s/form-button
                      :on-click #(rf/dispatch [::h/expand :dialog])}]
    (when id
      [ui/FlatButton {:label (:delete ph/button)
                      :secondary true
                      :on-click #(rf/dispatch [::model/delete id])}])]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [item]} params]
   [:div.container
    [form (:id params)]]))

