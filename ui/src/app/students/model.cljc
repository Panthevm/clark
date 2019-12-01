(ns app.students.model
  (:require [re-frame.core :as rf]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [_ [_ phase {{q :search} :params}]]
   (case phase
     (:init :params) {:method/get {:resource {:type :student}
                                   :params   {:ilike q}
                                   :req-id   :student}}
     nil)))

(rf/reg-sub
 index-page
 :<- [:xhr/response :student]
 (fn [student]
   {:items (:data student)}))
