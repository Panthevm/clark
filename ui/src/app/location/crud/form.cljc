(ns app.location.crud.form
  (:require [re-frame.core     :as rf]
            [zenform.model     :as zf]
            [app.helpers       :as h]))

(def schema-path [:form :location])
(def schema
  {:type   :form
   :fields {:id          {:type :string}
            :building    {:type :string}
            :number      {:type :string}
            :slots       {:type :string}
            :responsible {:type :string}}})

(defn evaling [db cb]
  (let [{:keys [errors value]} (->> schema-path (get-in db) zf/eval-form)]
    (when (empty? errors)
      (cb value))))

(rf/reg-event-fx
 ::init
 (fn [{db :db} [_ & [data]]]
   {:dispatch [:zf/init schema-path schema data]}))
