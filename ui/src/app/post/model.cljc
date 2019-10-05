(ns app.post.model
  (:require [re-frame.core :as rf]
            [app.helpers :as helpers]
            [clojure.string :as str]))

(def index-page   ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase params]]
   (let [params (get-in db [pid :params])]
     (case phase
       :init   {:xhr/fetch {:uri "/locations"
                            :req-id pid}}
       :deinit {:db (dissoc db pid)}))))

(rf/reg-sub
 index-page
 :<- [:xhr/response index-page]
 (fn [{data :data} _]
   data))

