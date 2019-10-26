(ns app.schedule.crud.form
  (:require [re-frame.core     :as rf]
            [zenform.model     :as zf]
            [zframes.mapper    :as zm]
            [app.form.events   :as event]
            [app.helpers       :as h]))

(def path [:form :schedule])
(def schema
  {:type   :form
   :fields {:id            {:type :string}
            :discipline    {:type :string}
            :group         {:type         :string
                            :display-path [:name]
                            :on-search    ::event/group}
            :resource_type {:type :string}}})

(defn evaling [db cb]
  (let [{:keys [errors value]} (->> path (get-in db) zf/eval-form)]
    (when (empty? errors)
      (cb value))))

(rf/reg-event-fx
 ::init
 (fn [_ [_ & [data]]]
   {:dispatch [:zf/init path schema data]}))


