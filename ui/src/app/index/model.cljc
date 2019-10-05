(ns app.index.model
  (:require [re-frame.core :as rf]
            [app.helpers :as helpers]
            [clojure.string :as str]))

(def index-page  ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase params]]
   (case phase
     :deinit {:db (dissoc db pid)}
     nil)))

(rf/reg-sub
 index-page
 :<- [:xhr/response :posts]
 :<- [:xhr/response index-page]
 (fn [[{posts :data} {status :data}] _]
   (merge
    {:posts (take 3 posts)}
    status)))
