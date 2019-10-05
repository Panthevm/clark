(ns app.post.view
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [app.pages :as pages]
            [app.helpers :as helpers]
            [clojure.string :as str]
            [app.post.model :as model]))


(defn item [{:keys [building number]}]
  [:div
   [:div building]
   [:div number]])

(pages/reg-subs-page
 model/index-page
 (fn [items]
   [:div.container
    [:h2.list-header.mt-5 "Кабинеты"]
    [:div.row.mt-4.post
     [:div.col-md-9
      (for [p items] ^{:key (:id p)}
        [:div.pb-4
         [item p]])]]]))
