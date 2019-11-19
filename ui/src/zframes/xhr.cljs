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
       (mapv (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))



(defn *json-fetch [{:keys [uri headers params success error] :as opts}]
  (let [{token :token base-url :base-url}    (get-in @db/app-db [:xhr/config])
        headers (merge (or headers {})
                       {"Content-Type" "application/json"
                        "authorization" (str "Bearer " token)})
        abort-controller (js/AbortController.)
        fetch-opts (-> (merge {:method "get" :mode "cors" :credentials "same-origin"} opts)
                       (dissoc :uri :headers :success :error :params)
                       (assoc :headers headers)
                       (assoc :signal (.-signal abort-controller)))
        fetch-opts (cond-> fetch-opts
                     (:body opts) (assoc :body (.stringify js/JSON (clj->js (:body opts)))))
        url (str base-url uri)
        timeout-timer (js/setTimeout
                       (fn []
                         (.abort abort-controller))
                       10000)]
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
                       [(:event e) {:request opts, :data data} (:params e)])])))))))))


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
 (fn [{db :db} [_ {{:keys [req-id]} :request :as resp}]]
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
