(ns app.location.crud.view
  (:require [re-frame.core            :as rf]
            [app.location.crud.form   :as form]
            [reagent-material-ui.core :as ui]
            [app.location.crud.model  :as model]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.form.inputs          :as i]
            [app.pages                :as pages]
            [app.placeholders         :as ph]))

(defn location-form [id]
  [:div
   [i/input form/schema-path [:building] {:hintText (:building ph/location)
                                          :floatingLabelText (:building ph/location)}] [:br]
   [i/input form/schema-path [:number] {:hintText (:number ph/location)
                                        :type "number"
                                        :floatingLabelText (:number ph/location)}] [:br]
   [i/input form/schema-path [:slots] {:hintText (:slots ph/location)
                                       :type "number"
                                       :floatingLabelText (:slots ph/location)}] [:br]
   [i/input form/schema-path [:responsible] {:hintText (:responsible ph/location)
                                             :floatingLabelText (:responsible ph/location)}] [:br]
   #_[ui/SelectField {:floatingLabelText (:sign ph/location)}
    [ui/MenuItem {:value "1"
                  :primaryText "Компьютеры"}]
    [ui/MenuItem {:value "2"
                  :primaryText "Проектор"}]]
   #_[:div.d-flex
    [ui/Chip {:onRequestDelete (fn []) :style s/chip}
     "Компьютеры"]
    [ui/Chip {:onRequestDelete (fn []) :style s/chip}
     "Проектор"]]
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
    [location-form (:id params)]]))
