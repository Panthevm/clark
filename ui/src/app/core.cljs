(ns ^:figwheel-hooks app.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as rf]

            [zframes.routing]
            [zframes.redirect]
            [zframes.xhr]
            [zframes.window-location]

            [app.location.view]
            [app.index.view]

            [app.helpers]
            [app.routes]
            [app.layout]
            [app.pages]))

(def default-config
  {:base-url     "http://localhost:3000"
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
  [:div.not-found
   [:h3 "Страница не найдена"]])

(defn current-page []
  (let [route (rf/subscribe [:route-map/current-route])]
    (fn []
      (let [page   (get @app.pages/pages (:match @route))
            params (:params @route)]
        [app.layout/layout
         (if page
           [page params]
           [not-found-page])]))))

(defn mount-root []
  (rf/dispatch-sync [::initialize])
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn ^:after-load re-render [] (mount-root))
