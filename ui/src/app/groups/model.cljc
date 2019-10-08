(ns app.groups.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase]]
   (case phase
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 (fn [db _]
   {}))


