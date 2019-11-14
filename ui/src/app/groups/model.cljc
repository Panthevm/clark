(ns app.groups.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [_ [_ phase]]
   (case phase
     :init   {:method/get {:resource {:type :group}
                           :req-id :group}}
     nil)))

(rf/reg-sub
 index-page
 :<- [:xhr/response :group]
 (fn [{groups :data} _]
   {:items groups}))
