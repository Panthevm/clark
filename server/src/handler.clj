(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            (resources
             [location :as location]
             [group    :as group])))

(defn group-routes []
  (context "/groups" []
           (GET     "/"    []    (response (group/get!)))
           (POST    "/"    req   (response (group/insert! req)))
           (GET     "/:id" [id]  (response (group/select! id)))
           (PUT     "/:id" req   (response (group/update! req)))
           (DELETE  "/:id" [id]  (response (group/delete! id)))))

(defn location-routes []
  (context "/locations" []
           (GET     "/"    []    (response (location/get!)))
           (POST    "/"    req   (response (location/insert! req)))
           (GET     "/:id" [id]  (response (location/select! id)))
           (PUT     "/:id" req   (response (location/update! req)))
           (DELETE  "/:id" [id]  (response (location/delete! id)))))

(defroutes handler
  (group-routes)
  (location-routes)
  (route/not-found (response {:message "not found"})))
