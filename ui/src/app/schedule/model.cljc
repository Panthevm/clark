(ns app.schedule.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase]]
   (case phase
     :init   {:method/get {:resource {:type :schedule}
                           :req-id :schedule}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 :<- [:xhr/response :schedule]
 (fn [{groups :data} _]
   {:items groups}))



