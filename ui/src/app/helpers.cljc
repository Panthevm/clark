(ns app.helpers
  (:require [clojure.string :as str]
            [chrono.core    :as cc]
            [chrono.now     :as cn]))

(defn href [& parts]
  (str "#/" (str/join "/" parts)))

(defn short-name [name]
  (let [v (str/split name #" ")]
    (str/join ". " [(first (v 1)) (first (v 2)) (v 0)])))

                                        ;Date
(def iso-fmt [:year "-" :month "-" :day "T" :hour ":" :min ":" :sec])
(def date-format
  {:iso [:year "-" :month "-" :day]
   :ru  [:day "." :month "." :year]})

(defn data-post [date]
  (-> date
      (cc/parse (:ru date-format))
      (cc/format (:iso date-format))))

(defn date-short-rus [value]
  (cc/format value (:ru date-format)))

(def now (cn/local))
