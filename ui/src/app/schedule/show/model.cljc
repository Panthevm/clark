(ns app.schedule.show.model
  (:require [re-frame.core          :as rf]
            [zenform.model          :as zf]
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
 (fn [{db :db} [_ {{data :resource} :data}]]
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
   {:idx-days    idx-days
    :shedule (get-in shedule [:data :resource])
    :group   (get-in group [:data :resource])}))

(rf/reg-event-fx
 ::add-column
 (fn [{db :db} _]
   {:dispatch [:zf/add-collection-item form/path [:schedule] {:date (h/date-short-rus h/now)} ]}))
