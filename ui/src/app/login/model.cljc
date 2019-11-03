(ns app.login.model
  (:require [re-frame.core :as rf]
            [app.login.form :as form]))

(def login-page  ::login-page)

(rf/reg-event-fx
 login-page
 (fn [_ [_ phase]]
   (when (= :init phase)
     {:dispatch [::form/init]})))

(rf/reg-sub
 login-page
 :<- [:pages/data login-page]
 (fn [page _]
   page))

(rf/reg-event-fx
 ::submit
 (fn []
   {:dispatch [::form/eval-form {:valid ::login}]}))

(rf/reg-event-fx
 ::login
 (fn [_ [_ form]]
   {:json/fetch {:uri         "/login"
                 :method      :post
                 :credentials "same-origin"
                 :success     {:event ::grant-access}
                 :body        form}}))

(rf/reg-event-fx
 ::grant-access
 (fn [{db :db} [_ {resp :data}]]
   (if (and (< (:status resp) 399) (get-in resp [:body :userinfo :id]))
     {:cookies/set {:key :asid :value (get-in resp [:cookies :asid :value])}}
     {:db (assoc-in db [login-page :error] "Неверно указана учетная запись или пароль.")})))
