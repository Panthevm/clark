(ns ^:figwheel-hooks app.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]

            [zframes.routing]
            [zframes.redirect]
            [zframes.cookies]
            [zframes.xhr]
            [zframes.method]
            [zframes.debounce]
            [zframes.window-location]

            [app.groups.view]
            [app.schedule.view]
            [app.schedule.show.view]
            [app.login.view]

            [app.routes]
            [app.layout :refer [layout]]
            [app.pages :refer [pages]]))

(def default-config
  {:base-url     "http://localhost:8080"
   :redirect_uri "http://localhost:8081"})

(defn ui-config [location]
  (merge default-config (:query-string location)))

(rf/reg-event-fx
 ::initialize
 [(rf/inject-cofx :window-location)]
 (fn [{:keys [location db]} _]
   (let [config (ui-config location)
         db     (merge db {:config config
                           :route-map/routes app.routes/routes
                           :xhr/config {:base-url (:base-url config)}})]
     {:route-map/start {}
      :db db})))

(defn not-found-page []
  [:div.container
   [:h3 "Страница не найдена"]])

(defn current-page []
  (let [route  @(rf/subscribe [:route-map/current-route])
        params (:params route)
        page   (get @pages (:match route))]
    [layout
     (if page
       [page params]
       [not-found-page])]))

(defn mount-root []
  (rf/dispatch-sync [::initialize])
  (r/render [current-page] (.getElementById js/document "app")))

(defn ^:after-load re-render [] (mount-root))
