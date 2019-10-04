(ns app
  (:require [compojure.handler :refer [api]]
            [ring.middleware.json :refer [wrap-json-params wrap-json-response]]
            [ring.middleware.cors :refer [wrap-cors]]
            [handler :as handler]))

(def app
  (-> (api handler/handler)
      (wrap-json-params)
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#"http://127.0.0.1:5500"]
                 :access-control-allow-methods [:get :put :post :delete])))
