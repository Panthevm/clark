(ns app.schedule.report.model
  (:require [re-frame.core          :as rf]
            [app.helpers            :as h]
            [app.form.events        :as event]))

(def index-page ::report)

(def student-path [:form :student])
(def student-schema
  {:type   :form
   :fields {:student {:type      :object
                      :display-paths [[:display]]
                      :on-click  ::students-report
                      :on-search ::event/student}}})

(rf/reg-event-fx
 ::students-report
 (fn [{db :db} [_ & [{id :id}]]]
   (let [schedule-id (get-in db [:fragment-params :id])]
     {:xhr/fetch {:uri    "/report"
                  :params {:subject  id
                           :schedule schedule-id}
                  :req-id :report}})))

(rf/reg-event-fx
 ::get-docx
 (fn [_ [_ report]]
   {:xhr/fetch {:uri    "/report/doc"
                :params {:avg        (:avg report)
                         :miss       (:miss report)
                         :discipline (:discipline report)
                         :proffessor (get-in report [:proffessor :username])
                         :student    (get-in report [:subject :display])
                         :group      (get-in report [:group :name])}
                :req-id :report}}))

(rf/reg-event-fx
 index-page
 (fn [_ [pid phase {id :id}]]
   (case phase
     :init   {:method/get {:resource {:type :schedule :id id}
                           :success  {:event ::success-get}
                           :req-id   :shedule}
              :dispatch   [::init]}
     :deinit {:dispatch [::h/clear-db [[pid] [:xhr :req :report]]]})))

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
 :<- [:xhr/response :report]
 (fn [[page schedule group report]]
   (merge page
          {:menu    (menu (:current-page page))
           :shedule (h/resource schedule)
           :group   (h/resource group)
           :report  (map :resource (h/entry report))})))

(rf/reg-event-db
 ::change-page
 (fn [db [_ page]]
   (assoc-in db [index-page :current-page] page)))

