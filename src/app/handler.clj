(ns app.handler
  (:require [reitit.ring :as ring]
            [app.actions :as action]))

(defn default [table]
  [(str "/" (name table))
   ["" {:get  {:handler (fn [req] (action/get! table req))}
        :post {:handler (fn [req] (action/insert! table req))}}]
   ["/:id" {:get    {:handler (fn [req] (action/select! table req))}
            :put    {:handler (fn [req] (action/update! table req))}
            :delete {:handler (fn [req] (action/delete! table req))}}]])

(def handler
  (ring/ring-handler
   (ring/router
    [(default :groups)
     #_(default :locations)
     #_(default :faculties)])
   (constantly {:status 404, :body "not found"})))
