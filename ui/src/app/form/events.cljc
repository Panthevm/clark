(ns app.form.events
  (:require [re-frame.core :as rf]
            [zenform.model :as zf]
            [clojure.string :as str]
            [app.helpers :as helpers]))

(rf/reg-event-fx
 ::group
 (fn [_ [pid & [{:keys [q path form-path] :as params}]]]

   {:method/get {:resource {:type :group}
                 :success  {:event ::group-loaded
                            :params {:form-path form-path
                                     :path path}}}
    :dispatch   [:zf/set form-path path :loading true]}))

(rf/reg-event-fx
 ::group-loaded
 (fn [{db :db} [_ {data :data} {:keys [form-path path]}]]

   (prn form-path path)
   (let [items (mapv (comp
                      (fn [{:keys [department] :as item}]
                        {:value   (select-keys item [:id :department :resource_type])
                         :display [:div
                                   [:text department]]})
                      :resource)
                     data)]
     {:db (assoc-in db (conj (into form-path (zf/get-node-path path)) :items) items)
      :dispatch [:zf/set form-path path :loading false]})))
