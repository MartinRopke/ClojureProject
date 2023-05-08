(ns my.service.server
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [com.mongodb MongoOptions ServerAddress]))

;; given host, given port
(let [conn (mg/connect {:host "localhost" :port 27017})])

(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")])

(println "\n!!!!!!COLECAO:!!!!!!!!\n")

;; get a collection
(let [conn (mg/connect)
      db   (mg/get-db conn "monger-test")
      coll "documents"]
;;   (mc/insert db coll {:first_name "John"  :last_name "Lennon"})
;;   (mc/insert db coll {:first_name "Ringo" :last_name "Starr"})

  (println (mc/find-maps db "documents")))
