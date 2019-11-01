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
(defn date-short-rus [value]
  (cc/format value [:month "." :day]))

(def now (cn/local))
