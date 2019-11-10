(ns zframes.auth
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 ::signin-success
 (fn [{db :db} [_ {data :data}]]
   {:db          (assoc-in db [:xhr/config :token] (:token data))
    :storage/set {:auth data}
    :dispatch    [::userinfo]
    :redirect    "#/profile"}))


(rf/reg-event-fx
 ::logout
 (fn [{db :db}]
   {:db (-> db (dissoc :user :xhr)
            (re-frame.utils/dissoc-in [:xhr/config :token]))
    :storage/remove [:auth]
    :redirect "#/login"}))

(rf/reg-event-fx
 ::userinfo
 (fn [{db :db}]
   {:json/fetch {:uri    "/info"
                 :method :get
                 :req-id :user}}))
