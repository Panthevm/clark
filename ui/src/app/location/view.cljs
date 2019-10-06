(ns app.location.view
  (:require [reagent-material-ui.core :as ui]
            [app.uikit :as kit]
            [app.placeholders :as ph]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [app.styles :as s]
            [app.pages :as pages]
            [app.helpers :as h]
            [clojure.string :as str]
            [app.location.model :as model]))

(defn location-form []
  [:div
   [ui/TextField {:hintText (:building ph/location)
                  :floatingLabelText (:building ph/location)}] [:br]
   [ui/TextField {:hintText (:number ph/location)
                  :type "number"
                  :floatingLabelText (:number ph/location)}] [:br]
   [ui/TextField {:hintText (:slots ph/location)
                  :type "number"
                  :floatingLabelText (:slots ph/location)}] [:br]
   [ui/TextField {:hintText (:responsible ph/location)
                  :floatingLabelText (:responsible ph/location)}] [:br]
   [ui/SelectField {:floatingLabelText (:sign ph/location)}
    [ui/MenuItem {:value "1"
                  :primaryText "Компьютеры"}]
    [ui/MenuItem {:value "2"
                  :primaryText "Проектор"}]]
   [:div.d-flex
    [ui/Chip {:onRequestDelete (fn []) :style s/chip}
     "Компьютеры"]
    [ui/Chip {:onRequestDelete (fn []) :style s/chip}
     "Проектор"]]
   [:div.form-buttons
    [ui/RaisedButton {:label (:save ph/button)
                      :style s/form-button :primary true}]
    [ui/RaisedButton {:label (:cancel ph/button)
                      :on-click #(rf/dispatch [::h/expand :dialog])}]]])

(defn Item
  [{:keys [building number]}]
  [ui/TableRow
   [ui/TableRowColumn building]
   [ui/TableRowColumn number]
   [ui/TableRowColumn number]
   [ui/TableRowColumn number]
   [ui/TableRowColumn number]])

(defn Table [items]
  [ui/Table
   [ui/TableHeader {:displaySelectAll  false
                    :adjustForCheckbox false}
    [ui/TableRow
     [ui/TableHeaderColumn {:tooltip (:building-desc ph/location)}
      (:building ph/location)]
     [ui/TableHeaderColumn {:tooltip (:number-desc ph/location)}
      (:number ph/location)]
     [ui/TableHeaderColumn {:tooltip (:slots-desc ph/location)}
      (:slots ph/location)]
     [ui/TableHeaderColumn {:tooltip (:sign-desc ph/location)}
      (:sign ph/location)]
     [ui/TableHeaderColumn {:tooltip (:responsible-desc ph/location)}
      (:responsible ph/location)]]]
   [ui/TableBody
    (for [p items]
      [Item p])]])

(defn Tolbar []
  (let [expands (h/expand? :dialog)
        expand #(rf/dispatch [::h/expand :dialog])]
    [ui/Toolbar
     [ui/ToolbarGroup
      [ui/ToolbarTitle {:text (:name ph/location)}]]
     [ui/ToolbarGroup
      [:div
       [ui/RaisedButton {:label (:create ph/button)
                         :on-click #(expand)
                         :primary true}]
       [ui/Dialog {:title (:create ph/location)
                   :autoScrollBodyContent true
                   :onRequestClose #(expand)
                   :open expands}
        [location-form]]]]]))

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div.container
    [Tolbar]
    [ui/TextField ph/search]
    [Table items]]))
