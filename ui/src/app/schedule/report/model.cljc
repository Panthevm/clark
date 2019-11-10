(ns app.schedule.report.model
  (:require [re-frame.core          :as rf]
            [app.helpers            :as h]
            [zenform.model          :as zenform]
            [app.form.events        :as event]
            [app.schedule.crud.form :as form]))

(def index-page ::report)

(def student-path [:form :student])
(def student-schema
  {:type   :form
   :fields {:student {:type      :object
                      :on-click  ::event/student
                      :on-search ::event/student}}})

(rf/reg-event-fx
 index-page
 (fn [{db :db} [pid phase {id :id}]]
   (case phase
     :init   {:method/get {:resource {:type :schedule :id id}
                           :success  {:event ::success-get}
                           :req-id   :shedule}
              :dispatch [::init]}
     :deinit {:db (dissoc db pid)})))

(rf/reg-event-fx
 ::init
 (fn []
   {:dispatch-n [[:zf/init student-path student-schema]]}))

(rf/reg-event-fx
 ::success-get
 (fn [_ [_ {{schedule :resource} :data}]]
   (let [id-group (get-in schedule [:group :id])]
     {:method/get {:resource {:type :group :id id-group}
                   :req-id   :group}})))

(defn menu [current]
  (let [menu [{:ico     [:i.far.fa-user]
               :display "Студент"}
              {:ico     [:i.far.fa-users]
               :display "Группа"}]]
    (map
     (fn [{display :display :as link}]
       (if (= display current)
         (assoc link :class " font-weight-bold")
         link))
     menu)))

(rf/reg-sub
 index-page
 :<- [:page/data index-page]
 :<- [:xhr/response :shedule]
 :<- [:xhr/response :group]
 (fn [[page schedule group]]
   (merge page
          {:menu    (menu (:current-page page))
           :shedule (h/resource schedule)
           :group   (h/resource group)})))

(rf/reg-event-db
 ::change-page
 (fn [db [_ page]]
   (assoc-in db [index-page :current-page] page)))

