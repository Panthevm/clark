(ns zframes.routing
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]
            [clojure.string :as str]
            [clojure.set :as set]
            [zframes.window-location :as window-location]
            [route-map.core :as route-map]))

(defn dispatch-routes [_]
  (let [fragment (.. js/window -location -hash)]
    (rf/dispatch [:fragment-changed fragment])))

(rf/reg-sub
 :route-map/fragment-params
 (fn [db _]
   (:fragment-params db)))

(rf/reg-sub
 :route-map/fragment
 (fn [db _]
   (:fragment db)))

(rf/reg-sub
 :route-map/current-route
 (fn [db _]
   (:route-map/current-route db)))

(rf/reg-sub
 :route-map/fragment-path
 (fn [db _]
   (:fragment-path db)))

(rf/reg-sub
 :route-map/location-search
 (fn [db _]
   (:location/search db)))

;; Experimental. Probably worth using for 403 and 404 errors displaying.
(rf/reg-sub-raw
 :route-map/error
 (fn [db _] (reaction (:route-map/error @db))))

(defn contexts-diff [route old-contexts new-contexts params old-params]
  (let [to-dispose (reduce (fn [acc [k o-params]]
                             (let [n-params (get new-contexts k)
                                   op (dissoc o-params :. :params)
                                   np (dissoc n-params :. :params)]
                               (if (= np op)
                                 acc
                                 (conj acc [k :deinit o-params]))))
                           [] old-contexts)

        to-dispatch (reduce (fn [acc [k n-params]]
                              (let [o-params (get old-contexts k)
                                    op (dissoc o-params :. :params)
                                    np (dissoc n-params :. :params)]
                                (cond
                                  (or (nil? op) (not= np op))
                                  (conj acc [k :init n-params])

                                  ;(and op (= (:.. np) route))     ;; Need to rethink this
                                  ;(conj acc [k :return n-params])

                                  :else acc)))
                            to-dispose new-contexts)]
    to-dispatch))

(defn parse-params [s]
  (if (str/blank? s)
    {}
    (reduce
     (fn [acc pair]
       (let [[k v] (str/split pair #"=" 2)]
         (assoc acc (keyword k) (js/decodeURIComponent v))))
     {} (str/split (str/replace s #"^\?" "") "&"))))

(defn dispatch-context [_]
  (let [query (.. js/window -location -search)]
    (rf/dispatch [:search-changed (parse-params query)])))

(rf/reg-event-fx
 :search-changed
 (fn [{db :db} [k search]]
   (let [old    (:location/search db)
         ctx-rs (:context/routes db)
         prev-h (get ctx-rs (:context old))]

     (if-not (= old search)
       (do
         (when prev-h
           {:dispatch [prev-h :deinit old], :db (dissoc db :navigation)})
         (if-let [handler (get ctx-rs (:context search))]
           {:dispatch [handler :init search],
            :db (assoc db :location/search search)}
           {:db (assoc db :location/search search)}))
       {:db (assoc db :location/search search)}))))

(defn parse-fragment [fragment]
  (let [[path params-str] (-> fragment
                              (str/replace #"^#" "")
                              (str/split #"\?"))
        params  (if (str/blank? params-str) {} (window-location/parse-querystring (or params-str "")))]
    {:path path
     :query-string params-str
     :params params}))


(rf/reg-event-fx
 :fragment-changed
 (fn [{db :db} [k fragment]]
   ;;(.scrollTo js/window 0 0)
   (let [{path :path q-params :params qs :query-string} (parse-fragment fragment)]
     (if-let [route (route-map/match [:. path] (:route-map/routes db))]
       (let [params (assoc (:params route) :params q-params)
             params (assoc params :search (:location/search db))
             route-match (:match route)
             route {:match route-match :params params :parents (:parents route)}
             contexts (reduce (fn [acc {ctx :context, r :.}]
                                (if ctx
                                  (assoc acc ctx (assoc params :.. r
                                                               :. route-match))
                                  acc)) {} (:parents route))
             current-page (:match route)
             old-page     (get-in db [:route-map/current-route :match])
             old-params (get-in db [:route-map/current-route :params])

             page-ctx-events (cond
                               (= current-page old-page)
                               (cond (= old-params params) []

                                     (= (dissoc old-params :params)
                                        (dissoc params :params))
                                     [[current-page :params params old-params]]

                                     :else
                                     [[current-page :deinit old-params] [current-page :init params]])
                               :else
                               (cond-> []
                                 old-page (conj [old-page :deinit old-params])
                                 true (conj [current-page :init params])))

             old-contexts (:route/context db)
             context-evs (contexts-diff (:match route) old-contexts contexts params old-params) 
             evs (into page-ctx-events context-evs)]
         {:db (assoc db
                     :route/history (conj (take 4 (:route/history db)) {:route (:match route) :uri fragment})
                     :fragment fragment
                     :fragment-params params
                     :fragment-path path
                     :fragment-query-string qs
                     :route/context contexts
                     :route-map/current-route route)
         :dispatch-n evs})
       {:db (assoc db
                   :fragment fragment
                   :route-map/current-route nil
                   :route-map/current-route nil
                   :route-map/error :not-found)}))))

(rf/reg-sub
 :pop-route
 (fn [db]
   (peek (:route-stack db))))

(rf/reg-event-db
 :clear-route-stack
 (fn [db _]
   (dissoc db :route-stack)))

(defn history-fx [_]
  (aset js/window "onhashchange" dispatch-routes)
  (dispatch-routes nil))

(rf/reg-fx :history history-fx)

(defn search-history-fx [_]
  (aset js/window "onpopstate" dispatch-context)
  (dispatch-context nil))

(rf/reg-fx :search-history search-history-fx)

(defn start-fx [_]
  (search-history-fx nil)
  (history-fx nil))

(rf/reg-fx :route-map/start start-fx)

(rf/reg-fx
 :route-map/redirect
 (fn [href] (aset (.-location js/window) "hash" {:uri href})))

(defn to-query-params [params]
  (->> params
       (map (fn [[k v]] (str (name k) "=" v)))
       (str/join "&")))

(defn to-hash [opts]
  (str "#" (:path opts) "?" (to-query-params (:params opts))))

(defn make-fragment [params]
  (let [opts  (parse-fragment (.. js/window -location -hash))]
    (to-hash (assoc opts :params params))))

(rf/reg-fx
 :route-map/set-params
 (fn [params]
   (let [opts  (parse-fragment (.. js/window -location -hash))]
     (aset (.. js/window -location) "hash" (to-hash (assoc opts :params params))))))
