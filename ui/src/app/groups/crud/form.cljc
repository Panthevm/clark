(ns app.groups.crud.form
  (:require [re-frame.core     :as rf]
            [zenform.model     :as zf]
            [app.helpers       :as h]
            [app.form.events   :as events]))

(def path [:form :group])
(def schema
  {:type   :form
   :fields {:id            {:type :string}
            :name          {:type :string}
            :faculty       {:type :string}
            :department    {:type :string}
            :course        {:type :integer}
            :students      {:type :collection
                            :item {:type      :object
                                   :on-search ::events/student}}
            :resource_type {:type :string}}})

(defn evaling [db cb]
  (let [{:keys [errors value]} (->> path (get-in db) zf/eval-form)]
    (when (empty? errors)
      (cb value))))

(rf/reg-event-fx
 ::init
 (fn [{db :db} [_ & [data]]]
   {:dispatch [:zf/init path schema data]}))

