(ns app.schedule.crud.form
  (:require [re-frame.core     :as rf]
            [zenform.model     :as zf]
            [app.helpers       :as h]))

(def path [:form :schedule])
(def schema
  {:type   :form
   :fields {:id            {:type :string}
            :group         {:type :string}
            :resource_type {:type :string}}})

(defn evaling [db cb]
  (let [{:keys [errors value]} (->> path (get-in db) zf/eval-form)]
    (when (empty? errors)
      (cb value))))

(rf/reg-event-fx
 ::init
 (fn [{db :db} [_ & [data]]]
   {:dispatch [:zf/init path schema data]}))


