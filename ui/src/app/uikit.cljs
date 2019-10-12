(ns app.uikit
  (:require [reagent-material-ui.core :as ui]
            [re-frame.core            :as rf]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.placeholders         :as ph]))

(defn color [nme] (aget ui/colors nme))

(defonce theme-defaults {:muiTheme (ui/getMuiTheme
                                    (-> ui/lightBaseTheme
                                        (js->clj :keywordize-keys true)
                                        (update :palette merge {:primary1Color (color "cyan500")
                                                                :primary2Color (color "cyan500")})
                                        clj->js))})

(defn ButtonCreate
  [on-click]
  [ui/RaisedButton {:label (:create ph/button)
                    :on-click on-click
                    :style s/form-button
                    :primary true}])

(defn ButtonSave
  [on-click]
  [ui/RaisedButton {:label (:save ph/button)
                    :on-click on-click
                    :style s/form-button
                    :primary true}])

(defn ButtonCancel
  [on-click]
  [ui/RaisedButton {:label (:cancel ph/button)
                    :style s/form-button
                    :on-click on-click}])

(defn ButtonDelete
  [on-click]
  [ui/FlatButton {:label (:delete ph/button)
                  :secondary true
                  :on-click on-click}])


(defn Dialog
  [content]
  [ui/Dialog {:title (:create ph/groups)
              :autoScrollBodyContent true
              :onRequestClose #(rf/dispatch [::h/expand :dialog])
              :open (h/expand? :dialog)}
   content])
