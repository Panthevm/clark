(ns app.handler
  (:require [reitit.ring :as ring]
            [app.actions :as a]
            (app.resources
             [group    :as group]
             [schedule :as schedule])))

(defn default [table]
  [(str "/" (name (:table table)))
   ["" {:get  {:parameters {:query {:ilike string?}
                            :ilike {:int string?}}
               :handler (fn [req] (a/-get  table req))}
        :post {:handler (fn [req] (a/-post table req))}}]
   ["/:id" {:get    {:handler (fn [req] (a/-select table req))}
            :put    {:handler (fn [req] (a/-put    table req))}
            :delete {:handler (fn [req] (a/-delete table req))}}]])

(def handler
  (ring/ring-handler
   (ring/router
    [(default group/table)
     (default schedule/table)])
   (constantly {:status 404, :body "not found"})))
