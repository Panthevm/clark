(ns app.schedule.crud.model
  (:require [re-frame.core          :as rf]
            [app.schedule.crud.form :as form]
            [app.helpers            :as h]))

(def index-page  ::show)
(def create-page ::create)

                                        ;Show page
(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase {id :id}]]
   (case phase
     :init   {:method/get {:resource {:type :schedule :id id}
                           :success  {:event ::success-get}}}
     :deinit {:db (dissoc db :form)})))

(rf/reg-sub
 index-page
 :<- [:form/path [:students]]
 (fn [idx-students _]
   {:idx-students idx-students}))

                                        ;Create page
(rf/reg-event-fx
 create-page
 (fn [{db :db} [pid phase {id :id}]]
   (case phase
     :init   {:dispatch [::form/init]}
     :deinit {:db (dissoc db :form)})))

(rf/reg-sub
 create-page
 :<- [:page/data create-page]
 (fn [_]
   {}))
                                        ;Events
(rf/reg-event-fx
 ::fcreate
 (fn [{db :db} _]
   (form/evaling db
                 (fn [value]
                   {:method/create {:resource (assoc value :resource_type :schedule)
                                    :success {:event ::main-redirect}}}))))

(rf/reg-event-fx
 ::success-get
 (fn [_ [_ {data :data}]]
   {:dispatch [::form/init (:resource data)]}))

(rf/reg-event-fx
 ::update
 (fn [{db :db}]
   (form/evaling db
                 (fn [value]
                   {:method/update {:resource value
                                    :success {:event ::main-redirect}}}))))

(rf/reg-event-fx
 ::delete
 (fn [{db :db} [_ id]]
   {:method/delete {:resource {:type :schedule :id id}}
    :redirect "#/schedule"}))

(rf/reg-event-fx
 ::main-redirect
 (fn [_ [_ {{{id :id} :resource} :data route :route}]]
   {:dispatch [:redirect (if-let [id (or id route)]
                           (h/href "schedule" id "show")
                           "#/schedule")]}))
