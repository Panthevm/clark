(ns zframes.redirect
  (:require [re-frame.core :as rf]
            [zframes.window-location :as window-location]
            [zframes.routing]))

(defn redirect [url]
  (set! (.-hash (.-location js/window)) url))

(rf/reg-fx
 :redirect
 (fn [opts]
   (redirect (str (:uri opts)
                  (when-let [params (:params opts)]
                    (window-location/gen-query-string params))))))

(rf/reg-event-fx
 :redirect
 (fn [_ [_ opts]]
   {:redirect {:uri opts}}))

(rf/reg-event-fx
 ::set-params
 (fn [{db :db} [_ params]]
   (let [pth (get db :fragment-path)]
     {:redirect {:uri pth
                 :params params}})))
