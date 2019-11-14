(ns zframes.method
  (:require [re-frame.core :as rf]))

(rf/reg-event-fx
 :method/get
 (fn [_ [_ {{:keys [type id]} :resource :as req}]]
   {:xhr/fetch  (-> req
                    (assoc :uri (str "/" (name type) (when id  (str "/" id))))
                    (dissoc :resource))}))

(rf/reg-event-fx
 :method/create
 (fn [_ [_ {{:keys [resource_type id] :as resource} :resource success :success :as req}]]
   {:xhr/fetch (-> req
                   (assoc :uri (str "/" (name resource_type) (when id  (str "/" id)))
                          :method "POST"
                          :body resource)
                   (dissoc :resource))}))

(rf/reg-event-fx
 :method/delete
 (fn [_ [_ {{:keys [type id] :as resource} :resource success :success :as req}]]
   {:xhr/fetch {:uri     (str "/" (name type) "/" id)
                :method  "DELETE"
                :success success}}))

(rf/reg-event-fx
 :method/update
 (fn [_ [_ {{:keys [resource_type id] :as resource} :resource success :success :as req}]]
   {:xhr/fetch (-> req
                   (assoc :uri (str "/" (name resource_type) (when id  (str "/" id)))
                          :method "PUT"
                          :body resource)
                   (dissoc :resource))}))

(rf/reg-fx :method/get    (fn [req] (rf/dispatch [:method/get req])))
(rf/reg-fx :method/update (fn [req] (rf/dispatch [:method/update req])))
(rf/reg-fx :method/create (fn [req] (rf/dispatch [:method/create req])))
(rf/reg-fx :method/delete (fn [req] (rf/dispatch [:method/delete req])))

