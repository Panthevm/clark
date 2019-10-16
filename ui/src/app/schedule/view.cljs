(ns app.schedule.view
  (:require [re-frame.core            :as rf]
            [app.schedule.model         :as model]
            [app.helpers              :as h]
            [app.styles               :as s]
            [app.helpers              :as h]
            [app.pages                :as pages]))

(pages/reg-subs-page
 model/index-page
 (fn [sidebar]
   [:div ]))

