(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            [resources.location :as location]))

(defn location-routes []
  (context "/locations" []
           (GET  "/"    []    (response (location/get!)))
           (GET  "/:id" [id]  (response (location/select! id)))
           (POST "/"    req   (response (location/insert! req)))
           (PUT  "/:id" req   (response (location/update! req)))))

(defroutes handler
  (location-routes)
  (route/not-found (response {:message "not found"})))
