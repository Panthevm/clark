(ns app.location.model
  (:require [re-frame.core     :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase]]
   (case phase
     :init   {:xhr/fetch {:uri "/locations"
                          :req-id :locations}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 :<- [:xhr/response :locations]
 (fn [{data :data} _]
   {:items data}))

