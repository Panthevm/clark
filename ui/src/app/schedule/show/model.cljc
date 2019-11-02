(ns app.schedule.show.model
  (:require [re-frame.core          :as rf]
            [app.helpers            :as h]
            [app.schedule.crud.form :as form]))

(def index-page ::show)

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase {id :id}]]
   (case phase
     :init   {:method/get {:resource {:type :schedule :id id}
                           :success  {:event ::success-get}
                           :req-id :shedule}}
     :deinit {:db (dissoc db pid)})))

(rf/reg-event-fx
 ::success-get
 (fn [_ [_ {{data :resource} :data}]]
   (let [id-group (get-in data [:group :id])]
     {:method/get {:resource {:type :group :id id-group}
                   :req-id   :group}
      :dispatch [::form/init data]})))

(rf/reg-sub
 index-page
 :<- [:zf/collection-indexes form/path [:schedule]]
 :<- [:page/data index-page]
 :<- [:xhr/response :group]
 :<- [:xhr/response :shedule]
 (fn [[idx-days page group shedule]]
   (merge page
    {:idx-days idx-days
     :shedule  (get-in shedule [:data :resource])
     :group    (get-in group [:data :resource])})))

(rf/reg-event-fx
 ::add-column
 (fn []
   {:dispatch [:zf/add-collection-item form/path [:schedule] {:date h/now}]}))

(rf/reg-event-db
 ::remove-select
 (fn [db]
   (re-frame.utils/dissoc-in db [index-page :selected-colum])))

(rf/reg-event-fx
 ::remove-column
 (fn [_ [_ idx]]
   {:dispatch-n [[::remove-select]
                 [:zf/remove-collection-item form/path [:schedule] idx]]}))

(rf/reg-event-db
 ::select-column
 (fn [db [_ idx]]
   (assoc-in db [index-page :selected-colum] idx)))
