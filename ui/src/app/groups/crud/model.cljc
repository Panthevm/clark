(ns app.groups.crud.model
  (:require [re-frame.core          :as rf]
            [app.groups.crud.form :as form]))

(def index-page  ::show)
(def create-page ::create)

                                        ;Show page
(rf/reg-event-fx
 index-page
 (fn [{db :db} [_ phase {id :id}]]
   (condp = phase
     :init   {:method/get {:resource {:type :group :id id}
                           :success  {:event ::success-get}}}
     :deinit {:db (dissoc db :form)})))

(rf/reg-sub
 index-page
 :<- [:zf/collection-indexes form/path [:students]]
 (fn [idx-students _]
   {:idx-students idx-students}))

                                        ;Create page

(rf/reg-event-fx
 create-page
 (fn [{db :db} [_ phase]]
   (condp = phase
     :init   {:dispatch [::form/init]}
     :deinit {:db (dissoc db :form)})))

(rf/reg-sub
 create-page
 :<- [:zf/collection-indexes form/path [:students]]
 (fn [idx-students _]
   {:idx-students idx-students}))

                                        ;Events
(rf/reg-event-fx
 ::fcreate
 (fn [{db :db} _]
   (form/evaling db
                 (fn [value]
                   {:method/create {:resource (assoc value :resource_type :group)
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
   {:method/delete {:resource {:type :group :id id}
                    :success  {:event ::main-redirect}}}))

(rf/reg-event-fx
 ::main-redirect (fn [_] {:redirect "#/groups"}))
