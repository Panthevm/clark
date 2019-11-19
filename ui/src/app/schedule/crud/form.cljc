(ns app.schedule.crud.form
  (:require [re-frame.core     :as rf]
            [zenform.model     :as zf]
            [app.form.events   :as event]))

(def path [:form :schedule])
(def schema
  {:type   :form
   :fields {:id            {:type :string}
            :discipline    {:type :string}
            :schedule      {:type :collection
                            :item {:type   :form
                                   :fields {:date       {:type :string}
                                            :assessment {:type :collection
                                                         :item {:type :object}}}}}
            :group         {:type          :string
                            :display-paths [[:name]]
                            :on-search     ::event/group}
            :resource_type {:type :string}}})


(defn import-data
  [data]
  (update data :schedule
          (fn [schedules]
            (mapv
             (fn [schedule]
               (update schedule :assessment
                       (fn [assesmets]
                         (mapv
                          (fn [{:keys [miss number subject] :as assesment}]
                            (if miss
                              {:number  number
                               :grade   "Н"
                               :subject subject}
                              assesment))
                          assesmets))))
             schedules))))

(defn export-data
  [data db]
  (-> data
      (update :schedule
              (fn [schedules]
                (mapv
                 (fn [schedule]
                   (update schedule :assessment
                           (fn [assesmets]
                             (mapv
                              (fn [{:keys [grade number subject] :as assesment}]
                                (if (= "Н" grade)
                                  {:number  number
                                   :miss    true
                                   :subject subject}
                                  assesment))
                              assesmets))))
                 schedules)))
      (assoc :proffessor (get-in db [:xhr :req :user :data :resource]))))

(defn evaling [db cb]
  (let [{:keys [errors value]} (->> path (get-in db) zf/eval-form)]
    (when (empty? errors)
      (cb (export-data value db)))))

(rf/reg-event-fx
 ::init
 (fn [_ [_ & [data]]]
   {:dispatch [:zf/init path schema (import-data data)]}))
