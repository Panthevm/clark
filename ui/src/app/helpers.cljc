(ns app.helpers
  (:require [clojure.string :as str]
            [re-frame.core  :as rf]
            [re-frame.utils :as utils]
            [chrono.core    :as cc]
            [chrono.now     :as cn]))

(defn href [& parts]
  (str "#/" (str/join "/" parts)))

(utils/dissoc-in {:a 1 :b {:c 2}} [[:a]])

(rf/reg-event-db
 ::dissoc-db
 (fn [db [_ & [paths]]]
   (reduce
    (fn [m path]
      (utils/dissoc-in m path))
    db paths)))


(defn resource [data]
  (get-in data [:data :resource]))

(defn entry [data]
  (get-in data [:data :entry]))
                                        ;Text

(defn remove-after [s end]
  (first (str/split s #"@")))

(defn short-name [name]
  (let [v (str/split name #" ")]
    (str/join ". " [(first (v 1)) (first (v 2)) (v 0)])))

                                        ;Date

(def iso-fmt [:year "-" :month "-" :day "T" :hour ":" :min ":" :sec])

(def date-format
  {:iso [:year "-" :month "-" :day]
   :ru  [:day "." :month "." :year]})

(def now (cc/format (cn/local) (:iso date-format)))

(defn data-post [date]
  (-> date
      (cc/parse (:ru date-format))
      (cc/format (:iso date-format))))

(defn date-get [date]
  (-> date
      (cc/parse iso-fmt)
      (cc/format (:ru date-format))))
