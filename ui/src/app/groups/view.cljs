(ns app.groups.view
  (:require [re-frame.core            :as rf]
            [reagent-material-ui.core :as ui]
            [app.groups.crud.form     :as form]
            [app.groups.crud.view     :as form-view]
            [app.groups.model         :as model]
            [app.placeholders         :as ph]
            [reagent.core             :as r]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.pages                :as pages]
            [app.uikit                :as kit]
            [app.groups.crud.view]))

(defn Item
  [{:keys [id department course students_number]}]
  [ui/TableRow {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (h/href "groups" id)}])
                :hoverable true}
   [ui/TableRowColumn department]
   [ui/TableRowColumn course]
   [ui/TableRowColumn students_number]])

(defn Table [items]
  [ui/Table
   [ui/TableHeader {:displaySelectAll  false
                    :adjustForCheckbox false}
    [ui/TableRow
     [ui/TableHeaderColumn {:tooltip (:department-desc ph/groups)}
      (:department ph/groups)]
     [ui/TableHeaderColumn {:tooltip (:course-desc ph/groups)}
      (:course ph/groups)]
     [ui/TableHeaderColumn {:tooltip (:students-desc ph/groups)}
      (:students ph/groups)]]]
   [ui/TableBody {:preScanRows false}
    (for [item items] ^{:key (:id item)}
      [Item item])]])

(defn Toolbar []
  (let [expands (h/expand? :dialog)
        expand #(rf/dispatch [::h/expand :dialog])]
    [ui/Toolbar
     [ui/ToolbarGroup
      [ui/ToolbarTitle {:text (:name ph/groups)}]]
     [ui/ToolbarGroup
      [:div
       [ui/RaisedButton {:label (:create ph/button)
                         :on-click #(do
                                      (rf/dispatch [::form/init])
                                      (expand))
                         :primary true}]
       [ui/Dialog {:title (:create ph/groups)
                   :autoScrollBodyContent true
                   :onRequestClose #(expand)
                   :open expands}
        [form-view/form]]]]]))

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [Toolbar]
    [ui/TextField ph/search]
    [Table items]]))
