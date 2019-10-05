(ns app.index.view
  (:require [re-frame.core :as rf]
            [app.styles :as styles]
            [app.pages :as pages]
            [app.helpers :as helpers]
            [app.index.model :as model]
            [clojure.string :as str]))

(pages/reg-subs-page
 model/index-page
 (fn [page]
   [:div  "index"]))
