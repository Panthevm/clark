(ns app.pages
  (:require [re-frame.core :as rf]))

(defonce pages (atom {}))

(defn subscribed-page [page-idx view]
  (fn [params]
    (let [m (rf/subscribe [page-idx])]
      (fn [] [view @m params]))))

(defn reg-subs-page
  "register subscribed page under keyword for routing"
  [key f & [layout-key]]
  (swap! pages assoc key (subscribed-page key f)))
