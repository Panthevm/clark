(ns app.groups.crud.model
  (:require [re-frame.core          :as rf]
            [app.groups.crud.form :as form]
            [clojure.string         :as str]
            [app.helpers            :as h]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase params]]
   (case phase
     :init   {:xhr/fetch {:uri (str "/groups/" (:id params))
                          :req-id pid
                          :success {:event ::success-get}}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 :<- [:xhr/response :locations]
 (fn [{data :data} _]
   {:items data}))

(rf/reg-event-fx
 ::success-get
 (fn [_ [_ {data :data}]]
   {:dispatch [::form/init data]}))

(rf/reg-event-fx
 ::update
 (fn [{db :db} [_ id]]
   (form/evaling db
                 (fn [value]
                   {:xhr/fetch {:uri     (str "/groups/" id)
                                :method  "PUT"
                                :body    value
                                :success {:event ::success-update}}}))))

(rf/reg-event-fx
 ::delete
 (fn [{db :db} [_ id]]
   {:xhr/fetch {:uri     (str "/groups/" id)
                :method  "DELETE"
                :success {:event ::success-delete}}}))

(rf/reg-event-fx
 ::success-delete
 (fn [{db :db} [_ {data :data}]]
   {:dispatch-n [[:zframes.redirect/redirect {:uri "/groups"}]
                 [::h/flash {:msg "Аудитория удалена"
                             :ts (:created_at	data)}]]}))

(rf/reg-event-fx
 ::success-update
 (fn [_ [_ {data :data}]]
   {:dispatch-n [[:zframes.redirect/redirect {:uri "/groups"}]
                 [::h/flash {:msg (str/join " " ["Группа «" (:department data) "» обновлена"])
                             :ts  (:created_at	data)}]]}))

(rf/reg-event-fx
 ::create
 (fn [{db :db} _]
   (form/evaling db
                 (fn [value]
                   {:xhr/fetch {:uri     "/groups"
                                :method  "POST"
                                :body    value
                                :success {:event ::create-success}}}))))
(rf/reg-event-fx
 ::create-success
 (fn [{db :db} [_ {data :data}]]
   {:db (update-in db [:xhr :req :groups :data]
                   (fn [items]
                     (into [] (concat [data] items))))
    :dispatch-n [[::h/flash {:msg (str/join " " ["Группа «" (:department data) "» создана"])
                             :ts (:created_at	data)}]
                 [::h/expand :dialog]]}))

