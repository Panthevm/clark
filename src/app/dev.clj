(ns app.dev
  (:require [immutant.web             :as web]
            [app.migration            :as migration]
            [app.handler              :refer [handler]]
            [environ.core             :refer (env)]
            [app.migration            :refer [migration]]
            (ring.middleware
             [cors :refer [wrap-cors]]
             [keyword-params :refer [wrap-keyword-params]]
             [params :refer [wrap-params]]
             [json :refer [wrap-json-body wrap-json-response]]))
  (:gen-class))

(def app
  (-> #'handler
      (wrap-keyword-params)
      (wrap-params)
      (wrap-json-body {:keywords? true})
      (wrap-json-response)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get :put :post :delete])))

(defn -main [& {:as args}]
  (migration)
  (web/run app
    (merge {"host" (env :demo-web-host), "port" (env :demo-web-port)}
           args)))
