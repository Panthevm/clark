(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            [resources.location :as location]))

(defn location-routes []
  (context "/locations" []
           (GET  "/" []  (response (location/get!)))
           (POST "/" req (response (location/insert! req)))))

(defroutes handler
  (location-routes)
  (route/not-found (response {:message "not found"})))
