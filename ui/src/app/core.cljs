(ns ^:figwheel-hooks app.core
  (:require [reagent.core  :as r]
            [re-frame.core :as rf]

            [zframes.routing]
            [zframes.redirect]
            [zframes.auth]
            [zframes.storage]
            [zframes.xhr]
            [zframes.method]
            [zframes.debounce]

            [app.groups.view]
            [app.students.view]
            [app.profile.view]
            [app.schedule.view]
            [app.login.view]

            [app.routes]
            [app.layout :as layout]
            [app.pages  :as pages]))

(def default-config
  {:base-url     "http://localhost:8080"
   :redirect_uri "http://localhost:8081"})

(rf/reg-event-fx
 ::initialize
 [(rf/inject-cofx :storage/get [:auth])]
 (fn [{{auth :auth} :storage db :db}]
   (let [db (merge db {:config           default-config
                       :route-map/routes #'app.routes/routes
                       :xhr/config       {:base-url (:base-url default-config)}})]
     (if auth
       {:db              (assoc-in db [:xhr/config :token] (:token auth))
        :route-map/start {}
        :dispatch        [:zframes.auth/userinfo]}
       {:db              db
        :route-map/start {}
        :redirect        "#/login"}))))

(defn not-found-page []
  [:div.container
   [:h3 "Страница не найдена"]])

(defn current-page []
  (let [route (rf/subscribe [:route-map/current-route])]
    (fn []
      (let [page   (get @pages/pages (:match @route))
            params (:params @route)]
        [layout/layout
         (if page
           [page params]
           [not-found-page])]))))

(defn ^:export mount-root []
  (rf/dispatch-sync [::initialize])
  (r/render [current-page] (.getElementById js/document "app")))

(defn ^:after-load re-render [] (mount-root))
