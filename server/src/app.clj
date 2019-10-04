(ns app
  (:require [compojure.handler    :refer [api]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [migration            :as migration]
            [ring.adapter.jetty   :as ring]
            [handler              :as handler]))

(def app
  (-> (api handler/handler)
      (wrap-json-params)
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#"http://127.0.0.1:5500"]
                 :access-control-allow-methods [:get :put :post :delete])))

(defn start [port]
  (ring/run-jetty app {:port port
                       :start? true}))

(defn -main []
  (migration/migration)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
