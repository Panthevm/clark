(ns app.auth
  (:require [buddy.auth.backends.token :as token]
            [buddy.auth.middleware     :as middleware]
            [app.resources.user        :as user]
            [cheshire.core             :as json]
            [app.db                    :refer [db]]
            [clj-pg.honey              :as pg]
            [buddy.sign.jwt            :as jwt]
            [buddy.auth                :as auth]
            [buddy.hashers             :as hashers]
            [clojure.data.codec.base64 :as b64]))

(defonce ^:private pkey
  (String.
   (b64/encode
    (.getBytes
     (apply str (take 5 (repeatedly #(str (rand)))))))
   "UTF-8"))

(def auth-backend (token/jws-backend {:secret pkey :options {:alg :hs512}}))

(defn wrap [req]
  (middleware/wrap-authentication req auth-backend))

(defn login
  [{{:keys [username password]} :body}]
  (let [{user :resource} (pg/query-first (db)
                                         {:select [:resource]
                                          :from   [:user]
                                          :where  ["@>" :resource
                                                   (json/generate-string {:username username})]})]
    (if (hashers/check password (get user :password))
      {:status 200
       :body   {:token (jwt/sign (dissoc user :password) pkey)}}
      {:status 404
       :body {}})))

(defn registration
  [{{:keys [username password]} :body}]
  (let [user (pg/create (db) user/table
                        {:resource {:username username
                                    :password (hashers/encrypt password)}})]
    {:status 201
     :body   {:token (jwt/sign (dissoc user :password) pkey)}}))
