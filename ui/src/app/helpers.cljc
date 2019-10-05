(ns app.helpers
  (:require [clojure.string :as str]
            [app.routes :as routes]
            [re-frame.core :as rf]
            [route-map.core :as route-map]))


(defn to-query-params [params]
  (->> params
       (map (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))

(defn href [& parts]
  (let [params (if (map? (last parts)) (last parts) nil)
        parts  (if params (butlast parts) parts)
        url    (str "/" (str/join "/" (map (fn [x] (if (keyword? x) (name x) (str x))) parts)))]
    (when-not  (route-map/match [:. url] routes/routes)
      (println (str url " is not matches routes")))
    (str "#" url (when params (str "?" (to-query-params params))))))

(defn dissoc-in
  [obj path]
  (cond
    (empty? path)
    obj
    (= (count path) 1)
    (dissoc obj (first path))
    :else
    (update-in obj (drop-last path) dissoc (last path))))

(rf/reg-event-db
 :xhr/loaded
 (fn [db [_ {:keys [data]} {:keys [pid key]}]]
   (-> db
       (assoc-in [pid :loading] false)
       (assoc-in [pid key]      data))))

(rf/reg-event-db
 :xhr/failed
 (fn [db [_ _ {:keys [pid]}]]
   (-> db
       (assoc-in [pid :loading] false)
       (assoc-in [pid :status] :error))))

