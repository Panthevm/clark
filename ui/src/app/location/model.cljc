(ns app.location.model
  (:require [app.location.form :as form]
            [clojure.string    :as str]
            [re-frame.core     :as rf]
            [app.helpers       :as h]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase]]
   (let [params (get-in db [pid :params])]
     (case phase
       :init   {:xhr/fetch {:uri "/locations"
                            :req-id pid}
                :dispatch [::form/init]}
       :deinit {:db (dissoc db pid)}))))

(rf/reg-sub
 index-page
 :<- [:xhr/response index-page]
 (fn [{data :data} _]
   {:items data}))

(rf/reg-event-fx
 ::create
 (fn [{db :db} _]
   (form/evaling db
                 (fn [value]
                   {:xhr/fetch {:uri     "/locations"
                                :method  "POST"
                                :body    value
                                :success {:event ::create-success}}}))))
(rf/reg-event-fx
 ::create-success
 (fn [{db :db} [_ {data :data}]]
   {:db (update-in db [:xhr :req index-page :data]
                   (fn [items]
                     (into [] (concat [data] items))))
    :dispatch-n [[::h/flash {:msg (str/join " " ["Аудитория «" (:building data) (:number data) "» создана"])
                             :ts (:created_at	data)}]
                 [::h/expand :dialog]]}))
