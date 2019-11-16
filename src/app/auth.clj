(ns app.auth
  (:require [buddy.auth.backends.token :as token]
            [clojure.string            :as str]
            [app.resources.user        :as user]
            [app.actions.user          :as user-action]
            [app.actions               :as a]
            [app.db                    :refer [db]]
            [clj-pg.honey              :as pg]
            [buddy.sign.jwt            :as jwt]
            [buddy.hashers             :as hashers]))

(def pkey "secret")

(def auth-backend
  (token/jws-backend {:secret pkey :options {:alg :hs512}}))

(defn login
  [{{:keys [username password]} :body}]
  (let [user (:resource (user-action/-get username))]
    (if (hashers/check password (:password user))
      (a/ok {:token (jwt/sign (dissoc user :password) pkey)})
      (a/error))))

(defn registration
  [{{:keys [username password]} :body}]
  (let [insert (pg/create (db) user/table
                          {:resource {:username username
                                      :password (hashers/encrypt password)}})
        user (:resource (a/make-resource insert user/table))]
    (a/created
     {:token (jwt/sign (dissoc user :password) pkey)})))

(defn info
  [{{auth "authorization"} :headers}]
  (let [token (last (str/split auth #" "))
        info {:resource (jwt/unsign token pkey)}]
    (a/ok info)))
