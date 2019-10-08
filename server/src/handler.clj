(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            [resources.location :as location]))

(defn location-routes []
  (context "/locations" []
           (GET     "/"    []    (response (location/get!)))
           (POST    "/"    req   (response (location/insert! req)))
           (GET     "/:id" [id]  (response (location/select! id)))
           (PUT     "/:id" req   (response (location/update! req)))
           (DELETE  "/:id" [id]  (response (location/delete! id)))))

(defroutes handler
  (location-routes)
  (route/not-found (response {:message "not found"})))
