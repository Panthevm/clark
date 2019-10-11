(ns app.faculty.view
  (:require [re-frame.core            :as rf]
            [reagent-material-ui.core :as ui]
            [app.faculty.crud.form     :as form]
            [app.faculty.crud.view     :as form-view]
            [app.faculty.model         :as model]
            [app.placeholders         :as ph]
            [reagent.core             :as r]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.pages                :as pages]
            [app.uikit                :as kit]
            [app.faculty.crud.view]))

(defn Item
  [{:keys [id name]}]
  [ui/TableRow {:on-click #(rf/dispatch [:zframes.redirect/redirect {:uri (h/href "faculties" id)}])
                :hoverable true}
   [ui/TableRowColumn name]])

(defn Table [items]
  [ui/Table
   [ui/TableHeader {:displaySelectAll  false
                    :adjustForCheckbox false}
    [ui/TableRow
     [ui/TableHeaderColumn (:faculty-name ph/faculty)]]]
   [ui/TableBody {:preScanRows false}
    (for [item items] ^{:key (:id item)}
      [Item item])]])

(defn Toolbar []
  (let [expands (h/expand? :dialog)
        expand #(rf/dispatch [::h/expand :dialog])]
    [ui/Toolbar
     [ui/ToolbarGroup
      [ui/ToolbarTitle {:text (:name ph/faculty)}]]
     [ui/ToolbarGroup
      [:div
       [ui/RaisedButton {:label (:create ph/button)
                         :on-click #(do
                                      (rf/dispatch [::form/init])
                                      (expand))
                         :primary true}]
       [ui/Dialog {:title (:create ph/faculty)
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
