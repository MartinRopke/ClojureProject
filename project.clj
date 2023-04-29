(defproject music-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring.adapter.jetty :as jetty]
                 [ring.middleware.json :as json]
                 [compojure.core :refer :all]
                 [clojure.java.jdbc :as jdbc]
                 [clojure.data.json :as data-json]] 
  :main ^:skip-aot music-api.core
  :repl-options {:init-ns music-api.core})
