(ns app.uikit
  (:require [reagent-material-ui.core :as ui]))

(defn color [nme] (aget ui/colors nme))

(defonce theme-defaults {:muiTheme (ui/getMuiTheme
                                    (-> ui/lightBaseTheme
                                        (js->clj :keywordize-keys true)
                                        (update :palette merge {:primary1Color (color "cyan500")
                                                                :primary2Color (color "cyan500")})
                                        clj->js))})
