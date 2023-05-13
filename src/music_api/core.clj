
;; ======= HTTP SERVER / MONGO SETUP ============
(ns music-api.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all] 
            [ring.middleware.json :as mj]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.db :as mdb])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:gen-class))

;; ===========================

;; ======= MUSIC SCHEMA ============


;; music {
;;   id: 1,
;;   title: "The title of the music",
;;   artist: "The artist of the music",
;;   year: 1999,
;;   genre: "The genre of the music",
;;   rating: 5
;; }

;; ===========================

;; ======= REBUILD DATABASE ============

(println "\n\n ======= REBUILD DATABASE ============ \n\n")

;; given host, given port
(def conn (mg/connect {:host "localhost" :port 27017}))
(def db (mg/get-db conn "monger-test"))


(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")])

(println (mc/find-maps db "documents"))

;; Drop database
(mdb/drop-db db)

;; Create collection
;; (mc/create db "musics")
;; Insert musics
(mc/insert db "musics" {:id 1 :title "Music 1" :artist "Artist 1" :year 1999 :genre "Genre 1" :rating 5})
(mc/insert db "musics" {:id 2 :title "Music 2" :artist "Artist 2" :year 1999 :genre "Genre 2" :rating 5})
(mc/insert db "musics" {:id 3 :title "Music 3" :artist "Artist 3" :year 1999 :genre "Genre 3" :rating 5})
(mc/insert db "musics" {:id 4 :title "Music 4" :artist "Artist 4" :year 1999 :genre "Genre 4" :rating 5})
(mc/insert db "musics" {:id 5 :title "Music 5" :artist "Artist 5" :year 1999 :genre "Genre 5" :rating 5})
(mc/insert db "musics" {:id 6 :title "Music 6" :artist "Artist 6" :year 1999 :genre "Genre 6" :rating 5})


;; get music by name
;; (println (mc/find-maps db "musics" {:title "Music 1"}))

(println "\n\n ========================== \n\n")

;; ======= ROUTES ============

(defn parse-music [music] (select-keys music [:id :title :artist :year :genre :rating]))

(defn parse-musics [musics] (for [m musics] (parse-music m) ))


(defn hello-world [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (->>
           (pp/pprint req)
           (str "Hello!!"))})

(defn get-all-musics [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (parse-musics (mc/find-maps db "musics"))})

(defn get-music-by-id [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (let [id (:id (:params req))]
           (pp/pprint id)
           (pp/pprint (parse-music (first (mc/find-maps db "musics" {:id (Integer/parseInt id)}))))
           (parse-music (first (mc/find-maps db "musics" {:id (Integer/parseInt id)}))))
   })

(defn add-music [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (let [body (:body req)]
           (pp/pprint (parse-music body))
           (mc/insert db "musics" (parse-music body))
           (str "Add music - Request body: " body))
   })

(defn update-music [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (let [body (:body req)]
           (pp/pprint body)
           (str "Update music - Request body: " body))
   })

(defn delete-music-by-id [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (let [id (:id (:params req))]
           (pp/pprint id)
           (str "Delte music by id - Request Id: " id))
   })

(defroutes app-routes
  (GET "/" [] (mj/wrap-json-response hello-world))
  (GET "/music" [] (mj/wrap-json-response get-all-musics))
  (GET "/music/:id" [] (mj/wrap-json-response get-music-by-id))
  (POST "/music" [] (mj/wrap-json-body add-music {:keywords? true}))
  (PUT "/music" [] (mj/wrap-json-body update-music {:keywords? true}))
  (DELETE "/music/:id" [] delete-music-by-id)
  (route/not-found "Error, page not found!"))

  (defn -main
  "This is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes api-defaults) {:port port})
    ; Run the server without ring defaults
    ;(server/run-server #'app-routes {:port port})
    (println (str "Running webserver at http:/127.0.0.1:" port "/"))))
