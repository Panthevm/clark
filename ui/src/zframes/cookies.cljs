(ns zframes.cookies
  (:refer-clojure :exclude [get set!])
  (:require [goog.net.cookies :as gcookies]
            [re-frame.core :as rf]
            [cljs.reader :as reader]))


(defn get-cookie [k]
  (reader/read-string (or (.get goog.net.cookies (name k)) "nil")))

(defn set-cookie [k v]
  (.set goog.net.cookies (name k) (clj->js v)))

(defn remove! [k] (.remove goog.net.cookies (name k)))

(rf/reg-cofx
 ::get
 (fn [coeffects key]
   (assoc-in coeffects [:cookie key] (get-cookie key))))

(rf/reg-fx
 ::set
 (fn [{k :key v :value}]
   (set-cookie k v)))

(rf/reg-fx
 :cookies/set
 (fn [{k :key v :value}]
   (set-cookie k v)))

(rf/reg-fx ::remove (fn [k] (.remove goog.net.cookies (name k))))
