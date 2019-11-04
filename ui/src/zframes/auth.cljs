(ns zframes.auth
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 ::get-token
 (fn [fx [_ code {id :client_id :as config}]]
   {:json/fetch {:uri "/auth/token"
                 :method "post"
                 :body {:client_id  id
                        :grant_type "authorization_code"
                        :code code}
                 :success {:event ::signin-success}}}))

(rf/reg-event-fx
 ::signin-success
 (fn [{db :db} [_ {data :data}]]
   {:db (assoc-in db [:xhr/config :token] (:token data))
    :storage/set {:auth data}}))


(rf/reg-event-fx
 ::logout
 (fn [{{config :config :as db} :db} _]
   {:json/fetch {:uri "/Session"
                 :method  :delete
                 :success {:event ::logout-done}
                 :error   {:event ::logout-done}}}))

(rf/reg-event-fx
 ::logout-done
 (fn [{{config :config :as db} :db} _]
   (merge {:db (dissoc db :user)
           :storage/remove [:auth :role] }
          (if-let [mu (:manager-url config)]
            {:zframes.redirect/page-redirect {:uri mu}}
            {:zframes.redirect/page-redirect {:uri "/"}}))))
