
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
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress])
  (:gen-class))

;; given host, given port
(let [conn (mg/connect {:host "localhost" :port 27017})])

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")])

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")
      coll "documents"]
  (println (mc/find-maps db "documents")))

;; ===========================


;; ======= ROUTES ============

(defn get-all-musics [req]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body (->>
           (pp/pprint req)
           (str "Get all musics - Request object: "))})

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
  (GET "/music" [] get-all-musics)
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
