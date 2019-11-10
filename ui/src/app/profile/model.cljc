(ns app.profile.model
  (:require [re-frame.core :as rf]
            [app.helpers   :as h]))

(def index ::index)

(rf/reg-event-fx
 index
 (fn []))

(rf/reg-sub
 index
 :<- [:xhr/response :user]
 (fn [user]
   {:user (h/resource user)}))
