(ns app.groups.view
  (:require [reagent-material-ui.core :as ui]
            [app.groups.model         :as model]
            [app.placeholders         :as ph]
            [re-frame.core            :as rf]
            [reagent.core             :as r]
            [app.helpers              :as h]
            [app.pages                :as pages]))


(pages/reg-subs-page
 model/index-page
 (fn [{:keys [items]}]
   [:div
    [ui/TextField ph/search]]))
