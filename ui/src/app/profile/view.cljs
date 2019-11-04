(ns app.profile.view
  (:require [re-frame.core     :as rf]
            [app.pages         :as pages]
            [app.profile.model :as model]))

(pages/reg-subs-page
 model/index
 (fn []
   [:div.container.segment.white
    [:h2 "Личный кабинет"]
    [:div.btn-form
     [:button.btn {:on-click #(rf/dispatch [::model/submit])}
      "Выйти"]]]))
