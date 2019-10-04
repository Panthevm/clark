(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            [resources.location :as location]))

(defn location-routes []
  (context "/locations" []
           (GET  "/" []                (response (location/get)))
           (POST "/" [building number] (response (location/insert building number)))))

(defroutes handler
  (location-routes)
  (route/not-found (response {:message "not found"})))
