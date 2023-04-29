(ns music-api.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as json]
            [compojure.core :refer :all]
            [clojure.java.jdbc :as jdbc]
            [clojure.data.json :as data-json]))

(def db-spec {:classname "org.sqlite.JDBC"
              :subprotocol "sqlite"
              :subname "music.db"})

(defn create-table []
  (jdbc/execute! db-spec
                 ["CREATE TABLE IF NOT EXISTS music
                   (id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT,
                    artist TEXT,
                    album TEXT)"]))

(defn- parse-body [body]
  (if (empty? body)
    {}
    (-> body
        (data-json/read-str))))

(defn- get-music-by-id [id]
  (jdbc/query
   db-spec
   ["SELECT * FROM music WHERE id = ?" id]
   {:row-fn :map}))

(defn- get-music-list []
  (jdbc/query
   db-spec
   ["SELECT * FROM music"]
   {:row-fn :map}))

(defn- insert-music [music]
  (jdbc/insert!
   db-spec
   :music
   music))

(defn- update-music [id music]
  (jdbc/update!
   db-spec
   :music
   {:id id}
   music))

(defn- delete-music [id]
  (jdbc/delete!
   db-spec
   :music
   {:id id}))

(defroutes app-routes
  (GET "/music" []
    (json/response
     {:status 200
      :body (get-music-list)}))

  (GET "/music/:id" [id]
    (json/response
     {:status 200
      :body (get-music-by-id id)}))

  (POST "/music" {body :body}
    (let [music (parse-body body)]
      (insert-music music)
      (json/response
       {:status 201
        :body music})))

  (PUT "/music/:id" [id body]
    (let [music (parse-body body)]
      (update-music id music)
      (json/response
       {:status 200
        :body music})))

  (DELETE "/music/:id" [id]
    (delete-music id)
    (json/response
     {:status 204})))

(def app
  (-> app-routes
      (json/wrap-json-body)
      (json/wrap-json-response)))

(defn -main []
  (create-table)
  (jetty/run-jetty app {:port 3000}))
