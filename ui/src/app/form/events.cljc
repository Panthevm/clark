(ns app.form.events
  (:require [re-frame.core :as rf]
            [zenform.model :as zf]
            [clojure.string :as str]
            [app.helpers :as helpers]))

(rf/reg-event-fx
 ::group
 (fn [_ [_ & [{:keys [q path form-path]}]]]
   {:method/get {:resource {:type :group}
                 :success  {:event ::group-loaded
                            :params {:form-path form-path
                                     :path path}}}
    :dispatch   [:zf/update-node-schema form-path path {:loading true}]}))

(rf/reg-event-fx
 ::group-loaded
 (fn [{db :db} [_ {data :data} {:keys [form-path path]}]]
   (let [items (mapv (comp
                      (fn [{:keys [name] :as item}]
                        {:value   (select-keys item [:id :name :resource_type])
                         :display [:text name]})
                      :resource)
                     data)]
     {:db (assoc-in db (conj (zf/get-full-path form-path path) :items) items)
      :dispatch-n [[:zf/update-node-schema form-path path {:display-path [:name]}]
                   [:zf/update-node-schema form-path path {:loading false}]]})))
