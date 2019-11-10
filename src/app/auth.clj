(ns app.auth
  (:require [buddy.auth.backends.token :as token]
            [buddy.auth.middleware     :as middleware]
            [clojure.string            :as str]
            [app.resources.user        :as user]
            [app.actions               :as a]
            [cheshire.core             :as json]
            [app.db                    :refer [db]]
            [clj-pg.honey              :as pg]
            [buddy.sign.jwt            :as jwt]
            [buddy.sign.jws            :as jws]
            [buddy.hashers             :as hashers]
            [clojure.data.codec.base64 :as b64]))

(defonce pkey
  (String.
   (b64/encode
    (.getBytes
     (clojure.string/join (repeatedly 5 (fn* [] (str (rand)))))))
   "UTF-8"))

(def auth-backend (token/jws-backend {:secret pkey :options {:alg :hs512}}))

(defn authentication [req]
  (middleware/wrap-authentication req auth-backend))

(defn authorization [req]
  (middleware/wrap-authorization req auth-backend))

(defn -get [username]
  (pg/query-first (db)
                  {:select [:*]
                   :from   [:user]
                   :where  ["@>" :resource
                            (json/generate-string {:username username})]}))

(defn login
  [{{:keys [username password]} :body}]
  (let [user (:resource (-get username))
        ss (prn user)]
    (if (hashers/check password (:password user))
      (a/ok {:token (jwt/sign (dissoc user :password) pkey)})
      (a/error))))

(defn registration
  [{{:keys [username password]} :body}]
  (let [insert (pg/create (db) user/table
                          {:resource {:username username :password (hashers/encrypt password)}})
        user (:resource (a/make-resource insert user/table))]
    (a/created
     {:token (jwt/sign (dissoc user :password) pkey)})))

(defn info
  [{{auth "authorization"} :headers}]
  (let [token (last (str/split auth #" "))
        info {:resource (jwt/unsign token pkey)}]
    (a/ok info)))
