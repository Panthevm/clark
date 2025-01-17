(ns app.login.view
  (:require [re-frame.core :as rf]
            [app.pages :as pages]
            [app.form.inputs :as inputs]
            [app.login.form :as form]
            [app.login.model :as model]))

(defn login-form []
  [:form.form
   [inputs/input form/form-path [:username]]
   [:div.mt-3
    [inputs/input form/form-path [:password] {:type "password"}]]])

(pages/reg-subs-page
 model/login-page
 (fn []
   [:div.d-flex.justify-content-center
    [:div.segment.white
     [:h2.text-center "Авторизация"]
     [login-form]
     [:div.btn-form.list-group
      [:button.btn {:on-click #(rf/dispatch [::model/submit])}
       "Войти"]
      [:button.btn {:on-click #(rf/dispatch [:redirect "#/registration"])}
       "Регистрация"]]]]))

(pages/reg-subs-page
 model/registration
 (fn []
   [:div.d-flex.justify-content-center
    [:div.segment.white
     [:h2.text-center "Регистрация"]
     [login-form]
     [:div.btn-form.list-group
      [:button.btn {:on-click #(rf/dispatch [::model/submit-reg])}
       "Зарегистрироваться"]
      [:button.btn {:on-click #(rf/dispatch [:redirect "#/login"])}
       "Войти"]]]]))
