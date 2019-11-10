(ns zframes.xhr
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as str]
            [re-frame.db :as db]
            [zframes.redirect]
            [re-frame.core :as rf]))

(defn sub-query-by-spaces
  [k s] (->> (str/split s #"\s+")
             (mapv (fn [v] (str (name k) "=" v)))
             (str/join "&")))

(defn to-query [params]
  (->> params
       (reduce-kv (fn [acc k v]
                    (if (or (nil? v) (str/blank? v))
                      acc
                      (assoc acc k v)))
                  {})
       (mapcat (fn [[k v]]
                 (cond
                   (vector? v) (mapv (fn [vv] (str (name k) "=" vv)) v)
                   (set? v) [(str (name k) "=" (str/join "," v))]
                   :else [(str (name k) "=" v)])))
       (str/join "&")))

(defn base-url [db url]
  (str (get-in db [:config :base-url]) url))

(defn *json-fetch [{:keys [uri format headers params success error] :as opts}]
  (let [{token :token base-url :base-url}    (get-in @db/app-db [:xhr/config])
        headers (merge (or headers {})
                       {"Content-Type" "application/json"
                        "authorization" (str "Bearer " token)})
        fetch-opts (-> (merge {:method "get" :mode "cors" :credentials "same-origin"} opts)
                       (dissoc :uri :headers :success :error :params)
                       (assoc :headers headers))
        fetch-opts (cond-> fetch-opts
                     (:body opts) (assoc :body (if (string? (:body opts))
                                                 (:body opts)
                                                 (.stringify js/JSON (clj->js (:body opts))))))
        url (str base-url uri)]
    (->
     (js/fetch (str url (when params (str "?" (to-query params)))) (clj->js fetch-opts))
     (.then
      (fn [resp]
        (.then (.json resp)
               (fn [doc]
                 (let [data (js->clj doc :keywordize-keys true)]
                   (mapv
                    #(when % (rf/dispatch %))
                    [(when (:req-id opts)
                       [:xhr/done {:request opts, :data data, :status (.-status resp)}])
                     (when-let [e (if (< (.-status resp) 299) success error)]
                       [(:event e) {:request opts, :data data} (:params e)])]))))))
     (.catch (fn [err]
               (rf/dispatch [(:event error)
                             (merge error {:request opts :error err})]))))))


(defn json-fetch [opts]
  (if (vector? opts)
    (doseq [o opts] (*json-fetch o))
    (*json-fetch opts)))

(rf/reg-fx ::json-fetch json-fetch)
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

(rf/reg-event-fx
 :xhr/remove-response
 (fn [{db :db} [_ req-id]]
   {:db (update-in db [:xhr :req] dissoc  req-id)}))

(rf/reg-sub
 :xhr/response
 (fn [db [_ req-id]]
   (get-in db [:xhr :req req-id])))

(rf/reg-event-fx
 :xhr/redirect
 (fn [_ [_ _ opts]]
   {:dispatch [:redirect opts]}))

(rf/reg-event-db
 ::fetch-start
 (fn [db [_ path]]
   (assoc db path true)))

(rf/reg-event-db
 ::fetch-end
 (fn [db [_ path]]
   (assoc db path false)))
