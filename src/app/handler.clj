(ns app.handler
  (:require [reitit.ring :as ring]
            [app.actions :as a]
            [app.auth    :as auth]
            (app.resources
             [group    :as group]
             [schedule :as schedule])))

(defn default [table]
  [(str "/" (name (:table table)))
   ["" {:get  {:handler (fn [req] (a/-get  table req))}
        :post {:handler (fn [req] (a/-post table req))}}]
   ["/:id" {:get    {:handler (fn [req] (a/-select table req))}
            :put    {:handler (fn [req] (a/-put    table req))}
            :delete {:handler (fn [req] (a/-delete table req))}}]])

(def handler
  (ring/ring-handler
   (ring/router
    [(default group/table)
     (default schedule/table)
     ["/registration" {:post {:handler (fn [req] (auth/registration req))}}]
     ["/login" {:post {:handler (fn [req] (auth/login req))}}]])
   (constantly {:status 404, :body "not found"})))
