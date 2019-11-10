(ns app.profile.view
  (:require [re-frame.core     :as rf]
            [zframes.auth      :as auth]
            [app.pages         :as pages]
            [app.profile.model :as model]))

(pages/reg-subs-page
 model/index
 (fn [{:keys [user]}]
   [:div.container
    [:div.row
     [:div.col-sm-3.segment.shadow.white
      [:h2 "Профиль"]
      [:span "Почта: " [:text.text-muted (:username user)]]
      [:div.btn-form
       [:button.btn {:on-click #(rf/dispatch [::auth/logout])}
        "Выйти"]]]
     [:div.col-sm.segment.shadow.white.ml-md-3.mt-3.mt-md-0.form.form
      [:h2 "Статистика"]]]]))
