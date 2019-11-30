(ns app.helpers
  (:require [clojure.string :as str]
            [chrono.core    :as cc]
            [re-frame.core  :as rf]
            [chrono.now     :as cn]))

(defn dispatch-n [events]
  (doseq [event events]
    (when event
      (rf/dispatch event))))

(defn href [& parts]
  (str "#/" (str/join "/" parts)))

(defn resource [data]
  (get-in data [:data :resource]))

(defn entry [data]
  (get-in data [:data :entry]))
                                        ;Text

(defn remove-after [s end]
  (first (str/split s (re-pattern end))))

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
