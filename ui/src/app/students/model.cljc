(ns app.students.model
  (:require [re-frame.core :as rf]
            [app.helpers   :as h]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [_ [_ phase]]
   (case phase
     :init {:method/get {:resource {:type :student}
                         :req-id   :student}}
     nil)))

(rf/reg-sub
 index-page
 :<- [:xhr/response :student]
 (fn [student]
   {:items (:data student)}))
