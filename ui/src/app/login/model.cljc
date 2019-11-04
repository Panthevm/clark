(ns app.login.model
  (:require [re-frame.core :as rf]
            [app.login.form :as form]))

(def login-page    ::login-page)
(def registration  ::registration)

(rf/reg-event-fx
 login-page
 (fn [_ [_ phase]]
   (when (= :init phase)
     {:dispatch [::form/init]})))


(rf/reg-sub
 login-page
 (fn [] {}))

(rf/reg-event-fx
 registration
 (fn [_ [_ phase]]
   (when (= :init phase)
     {:dispatch [::form/init]})))

(rf/reg-sub
 registration
 (fn [] {}))

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
 ::submit-reg
 (fn []
   {:dispatch [::form/eval-form {:valid ::registration}]}))

(rf/reg-event-fx
 ::registration
 (fn [_ [_ form]]
   {:json/fetch {:uri         "/registration"
                 :method      :post
                 :credentials "same-origin"
                 :body        form}}))

(rf/reg-event-fx
 ::grant-access
 (fn [_ [_ {resp :data}]]
   {:dispatch-n [[:cookies/set {:key :asid :value (get resp :token)}]
                 [:redirect "#/groups"]]}))
