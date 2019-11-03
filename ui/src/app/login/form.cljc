(ns app.login.form
  (:require [re-frame.core :as rf]
            [zenform.model :as zf]
            [clojure.string :as str]))

(def form-path [:form :login])
(def form-schema
  {:type :form
   :fields {:username {:type :string
                       :validators {:required {:message "Поле обязательно для заполнения."}}}
            :password {:type :string
                       :validators {:required {:message "Поле обязательно для заполнения."}}}}})

(rf/reg-event-fx
 ::init
 (fn [{db :db} [_ init] ]
   {:dispatch [:zf/init form-path form-schema init]}))

(rf/reg-event-fx
 ::eval-form
 (fn [{db :db} [_ {valid :valid}]]
   (let [{:keys [errors value]} (->> form-path (get-in db) zf/eval-form)]
     (when (empty? errors)
       {:dispatch [valid value]}))))
