(ns handler
  (:use ring.util.response)
  (:require [compojure.route    :as route]
            [compojure.core     :refer :all]
            [actions            :as action]
            (resources
             [location :as location]
             [group    :as group])))

(defn group-routes []
  (context "/groups" []
           (GET     "/"    []    (response (action/get    :groups)))
           (POST    "/"    req   (response (action/insert :groups req)))
           (GET     "/:id" [id]  (response (action/select :groups id)))
           (PUT     "/:id" req   (response (action/update :groups req)))
           (DELETE  "/:id" [id]  (response (action/delete :groups id)))))

(defn location-routes []
  (context "/locations" []
           (GET     "/"    []    (response (action/get    :locations)))
           (POST    "/"    req   (response (action/insert :locations req)))
           (GET     "/:id" [id]  (response (action/select :locations id)))
           (PUT     "/:id" req   (response (action/update :locations req)))
           (DELETE  "/:id" [id]  (response (action/delete :locations id)))))

(defroutes handler
  (group-routes)
  (location-routes)
  (route/not-found (response {:message "not found"})))
