(defproject music-api "0.1.0-SNAPSHOT"
  :description "Music API CRUD with mongo"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 ; Compojure - A basic routing library
                 [compojure "1.6.1"]
                 ; Our Http library for client/server
                 [http-kit "2.3.0"]
                 ; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.2"]
                 ; Clojure data.JSON library
                 [org.clojure/data.json "0.2.6"]
                 [ring/ring-json "0.5.0"]] 
  :main ^:skip-aot music-api.core
  :repl-options {:init-ns music-api.core})
