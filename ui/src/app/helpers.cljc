(ns app.helpers
  (:require [clojure.string :as str]
            [chrono.core    :as cc]
            [chrono.now     :as cn]))

(defn href [& parts]
  (str "#/" (str/join "/" parts)))

(defn short-name [name]
  (let [v (str/split name #" ")]
    (str/join ". " [(first (v 1)) (first (v 2)) (v 0)])))

(defn resource [data]
  (get-in data [:data :resource]))
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
