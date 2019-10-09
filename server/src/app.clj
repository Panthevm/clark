(ns app
  (:require [org.httpkit.server   :refer [run-server]]
            [handler              :refer [handler]]
            [migration            :refer [migration]]
            (ring.middleware
             [cors :refer [wrap-cors]]
             [json :refer [wrap-json-body wrap-json-response]])))

(def app
  (-> handler/handler
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "8080"))]
    (migration)
    (run-server app {:port port})
    (println (str "Running http:/127.0.0.1:" port))))
