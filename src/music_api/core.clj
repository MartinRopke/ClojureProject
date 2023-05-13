
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
(mc/insert db "musics" {:title "Music 1" :artist "Artist 1" :year 1999 :genre "Genre 1" :rating 5})
(mc/insert db "musics" {:title "Music 2" :artist "Artist 2" :year 1999 :genre "Genre 2" :rating 5})
(mc/insert db "musics" {:title "Music 3" :artist "Artist 3" :year 1999 :genre "Genre 3" :rating 5})
(mc/insert db "musics" {:title "Music 4" :artist "Artist 4" :year 1999 :genre "Genre 4" :rating 5})
(mc/insert db "musics" {:title "Music 5" :artist "Artist 5" :year 1999 :genre "Genre 5" :rating 5})
(mc/insert db "musics" {:title "Music 6" :artist "Artist 6" :year 1999 :genre "Genre 6" :rating 5})


(println "\n\n ========================== \n\n")

;; ======= ROUTES ============


;; (defn get-docs [] 
;;   (let [conn (mg/connect)
;;         db   (mg/get-db conn "monger-test")
;;         coll "documents"]
;;     (mc/find-maps db "documents")))  
  

;; (defn hello-world[req]
;;   {:status  200
;;    :headers {"Content-Type" "application/json"}
;;    :body ( for [d (get-docs)] (select-keys d [:first_name :last_name]))
;;   })

(defn hello-world [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (->>
           (pp/pprint req)
           (str "Hello!!"))})

(defn get-all-musics [req]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body (mc/find-maps db "musics")})

(defn get-music-by-id [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (let [id (:id (:params req))]
           (pp/pprint id)
           (str "Get music by id - Request Id: " id))
   })

(defn add-music [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (let [body (:body req)]
           (pp/pprint body)
           (str "Add music - Request body: " body))
   })

(defn update-music [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (let [body (:body req)]
           (pp/pprint body)
           (str "Update music - Request body: " body))
   })

(defn delete-music-by-id [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (let [id (:id (:params req))]
           (pp/pprint id)
           (str "Delte music by id - Request Id: " id))
   })

(defroutes app-routes
  (GET "/" [] (mj/wrap-json-response hello-world))
  (GET "/music" [] (mj/wrap-json-body get-all-musics))
  (GET "/music/:id" [] get-music-by-id)
  (POST "/music" [] (mj/wrap-json-body add-music))
  (PUT "/music" [] (mj/wrap-json-body update-music))
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
