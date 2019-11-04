(ns app.profile.model
  (:require [re-frame.core :as rf]))

(def index ::index)

(rf/reg-event-fx
 index
 (fn [{db :db} [_ phase]]
   {:json/fetch {:uri         "/info"
                 :method      :get}}
   ))

(rf/reg-sub
 index
 (fn [] {}))
