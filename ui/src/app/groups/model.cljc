(ns app.groups.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase]]
   (case phase
     :init   {:xhr/fetch {:uri "/groups"
                          :req-id :groups}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 :<- [:xhr/response :groups]
 (fn [{data :data} _]
   {:items data
    :ss (prn "######3")}))

