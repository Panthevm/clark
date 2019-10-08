(ns app.location.view
  (:require [reagent-material-ui.core :as ui]
            [app.location.crud.form   :as form]
            [app.location.crud.view   :as form-view]
            [app.location.model       :as model]
            [app.placeholders         :as ph]
            [re-frame.core            :as rf]
            [reagent.core             :as r]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.pages                :as pages]
            [app.uikit                :as kit]
            
            [app.location.crud.view]))

(defn Item
  [{:keys [id building number slots responsible]}]
  [ui/TableRow {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (h/href "locations" id)}])
                :hoverable true}
   [ui/TableRowColumn building]
   [ui/TableRowColumn number]
   [ui/TableRowColumn slots]
   [ui/TableRowColumn number]
   [ui/TableRowColumn responsible]])

(defn Table [items]
  [ui/Table {:width "100px"}
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
   [ui/TableBody {:preScanRows false}
    (for [item items] ^{:key (:id item)}
      [Item item])]])

(defn Toolbar []
  (let [expands (h/expand? :dialog)
        expand #(rf/dispatch [::h/expand :dialog])]
    [ui/Toolbar
     [ui/ToolbarGroup
      [ui/ToolbarTitle {:text (:name ph/location)}]]
     [ui/ToolbarGroup
      [:div
       [ui/RaisedButton {:label (:create ph/button)
                         :on-click #(do
                                      (rf/dispatch [::form/init])
                                      (expand))
                         :primary true}]
       [ui/Dialog {:title (:create ph/location)
                   :autoScrollBodyContent true
                   :onRequestClose #(expand)
                   :open expands}
        [form-view/location-form]]]]]))

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [Toolbar]

    [ui/TextField ph/search]
    [Table items]]))
