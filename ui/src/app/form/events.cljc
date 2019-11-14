(ns app.form.events
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 ::group
 (fn [_ [_ & [{:keys [q path form-path]}]]]
   {:xhr/fetch {:uri    "/group"
                :params {:ilike q}
                :success  {:event ::group-loaded
                           :params {:path path :form-path form-path :q q}}}
    :dispatch [:zf/update-node-schema form-path path {:loading true}]}))

(rf/reg-event-fx
 ::group-loaded
 (fn [_ [_ {data :data} {:keys [form-path path q]}]]
   (let [items (mapv (comp
                      (fn [{:keys [name] :as item}]
                        {:value   (select-keys item [:id :name :resource_type])
                         :display [:text name]})
                      :resource)
                     data)]
     {:dispatch [:zf/update-node-schema form-path path (merge {:display-path [:name]
                                                               :loading false}
                                                              (if q
                                                                {:items items}
                                                                {:default-items items}))]})))

(rf/reg-event-fx
 ::student
 (fn [_ [_ & [{:keys [q path form-path]}]]]
   {:xhr/fetch {:uri     "/student"
                :params  {:ilike q}
                :success {:event  ::student-loaded
                          :params {:path path :form-path form-path :q q}}}
    :dispatch  [:zf/update-node-schema form-path path {:loading true}]}))

(rf/reg-event-fx
 ::student-loaded
 (fn [_ [_ {data :data} {:keys [form-path path q]}]]
   (let [items (mapv (comp
                      (fn [{:keys [id name resource_type] :as item}]
                        {:value   {:id id
                                   :display name
                                   :resource_type resource_type}
                         :display [:text name]})
                      :resource)
                     data)]
     {:dispatch [:zf/update-node-schema form-path path (merge {:loading      false
                                                               :display-path [:display]}
                                                              (if q
                                                                {:items items}
                                                                {:default-items items}))]})))
