(ns app.pages
  (:require [re-frame.core :as rf]))

(defonce pages (atom {}))

(rf/reg-sub
 :page/data
 (fn [db [_ pid]]
   (get db pid)))

(defn subscribed-page [page view]
  (fn [params]
    (let [m (rf/subscribe [page])]
      (fn [] [view @m params]))))

(defn reg-subs-page [key f]
  (swap! pages assoc key (subscribed-page key f)))
