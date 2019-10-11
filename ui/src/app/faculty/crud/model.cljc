(ns app.faculty.crud.model
  (:require [re-frame.core          :as rf]
            [app.faculty.crud.form  :as form]
            [clojure.string         :as str]
            [app.helpers            :as h]))

(def index-page ::index)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase {id :id}]]
   (case phase
     :init   {:method/get {:resource {:type :faculties :id id}
                           :success {:event ::success-get}
                           :req-id pid}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-sub
 index-page
 :<- [:page/data index-page]
 (fn [_ _]
   {}))

(rf/reg-event-fx
 ::success-get
 (fn [_ [_ {data :data}]]
   {:dispatch [::form/init data]}))

(rf/reg-event-fx
 ::update
 (fn [{db :db} [_ id]]
   (form/evaling db
    (fn [value]
      {:method/update {:resource value
                       :success {:event ::success-update}}}))))

(rf/reg-event-fx
 ::delete
 (fn [{db :db} [_ id]]
   {:method/delete {:resource {:type :faculties :id id}
                    :success {:event ::success-delete}}}))

(rf/reg-event-fx
 ::success-delete
 (fn [{db :db} [_ {data :data}]]
   {:dispatch-n [[:zframes.redirect/redirect {:uri "/faculties"}]
                 [::h/flash {:msg (str "Факультет" (:name data) " удален")
                             :ts (:created_at	data)}]]}))

(rf/reg-event-fx
 ::success-update
 (fn [_ [_ {data :data}]]
   {:dispatch [:zframes.redirect/redirect {:uri "/faculties"}]}))

(rf/reg-event-fx
 ::create
 (fn [{db :db} _]
   (form/evaling db
                 (fn [value]
                   {:method/create {:resource value
                                    :success {:event ::create-success}}}))))
(rf/reg-event-fx
 ::create-success
 (fn [{db :db} [_ {data :data}]]
   {:db (update-in db [:xhr :req :groups :data]
                   (fn [items]
                     (into [] (concat [data] items))))
    :dispatch [::h/expand :dialog]}))

