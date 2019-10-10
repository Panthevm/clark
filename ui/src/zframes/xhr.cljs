(ns zframes.xhr
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as str]
            [re-frame.db :as db]
            [zframes.redirect]
            [re-frame.core :as rf]))

(defn to-query [params]
  (->> params
       (mapv (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))

(defn base-url [db url]
  (str (get-in db [:config :base-url]) url))

(defn *json-fetch [{:keys [uri format headers cookies is-fetching-path params success error] :as opts}]
  (let [{token :token base-url :base-url}    (get-in @db/app-db [:xhr/config])
        headers (merge (or headers {})
                       {"Content-Type" "application/json"
                        "Authorization" (str "Bearer " token)})
        fetch-opts (-> (merge {:method "get" :mode "cors" :credentials "same-origin"} opts)
                       (dissoc :uri :headers :success :error :params :files)
                       (assoc :headers headers))
        fetch-opts (if (:body opts) (assoc fetch-opts :body (.stringify js/JSON (clj->js (:body opts)))))
        url (str base-url uri)]
    (->
     (js/fetch (str url (when params (str "?" (to-query params)))) (clj->js fetch-opts))
     (.then
      (fn [resp]
        (.then (.json resp)
               (fn [doc]
                 (let [data (js->clj doc :keywordize-keys true)]
                   (->> [(when (:req-id opts)
                           [:xhr/done
                            {:request opts
                             :data data
                             :status (.-status resp)}])
                         (when-let [e (if (< (.-status resp) 299) success error)]
                           [(:event e)
                            {:request opts
                             :data data}
                            (:params e)])]
                        (mapv #(rf/dispatch %))))))))
     {})))

(defn json-fetch [opts]
  (if (vector? opts)
    (doseq [o opts] (*json-fetch o))
    (*json-fetch opts)))

(rf/reg-fx :json/fetch json-fetch)
(rf/reg-fx :xhr/fetch #(rf/dispatch [:xhr/fetch %]))

(rf/reg-event-fx
 :xhr/fetch
 (fn [{db :db} [_ opts]]
   {:db (reduce (fn [acc opt]
                  (assoc-in acc [:xhr :req (:req-id opt) :loading] true))
                db
                (if (vector? opts) opts [opts]))
    :json/fetch opts}))

(rf/reg-event-fx
 :xhr/done
 (fn [{db :db} [_ {{:keys [req-id] :as req} :request :as resp}]]
   {:db (assoc-in db [:xhr :req req-id] resp)}))

(rf/reg-sub
 :xhr/response
 (fn [db [_ req-id]]
   (get-in db [:xhr :req req-id])))
