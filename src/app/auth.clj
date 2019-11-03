(ns app.auth
  (:require (buddy
             [auth.backends.token :as token]
             [auth.middleware     :as middleware]
             [sign.jws            :as jws]
             [auth                :as auth]
             [hashers             :as hashers])
            [clojure.data.codec.base64 :as b64]))

(defonce ^:private pkey
  (String.
   (b64/encode
    (.getBytes
     (apply str (take 5 (repeatedly #(str (rand)))))))
   "UTF-8"))

(def auth-backend (token/jws-backend {:secret pkey :options {:alg :hs512}}))

(defn wrap []
  (middleware/wrap-authentication auth-backend))

(def users {"admin" {:username "admin"
                     :hashed-password (hashers/encrypt "adminpass")
                     :roles #{:user :admin}}
            "user"  {:username "user"
                     :hashed-password (hashers/encrypt "userpass")
                     :roles #{:user}}})

(defn lookup-user [username password]
  (if-let [user (get users username)]
    (when (hashers/check password (get user :hashed-password))
      (dissoc user :hashed-password))))

(defn do-login [{{username "username" password "password"} :params}]
  (if-let [user (lookup-user username password)]
    {:status 200
     :headers {:content-type "application/json"}
     :body {:token (jws/sign user pkey)}}
    (prn "/login")))

(defn auth? [req]
  (auth/authenticated? req))

(defn is-admin [{user :identity}]
  (contains? (apply hash-set (:roles user)) :admin))

