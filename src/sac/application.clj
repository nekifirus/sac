(ns sac.application
  "Application entrypoint
  We can simple change concrete implementations here"
  (:require [sac.scopus-api :refer [->ScopusApi]]
            [sac.access-control :refer [->AC]]
            [sac.json-api :refer [build-api]]
            [sac.sql-repository :refer [build-sql-datasource init-repo ->SQLRepository]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(def ds (build-sql-datasource
         {:dbtype "postgresql"
          :dbname "sac_db"
          :user (System/getenv "DB_USER")
          :password (System/getenv "DB_PASSWORD")
          :host (System/getenv "DB_HOST")}))

(init-repo ds)
(def repo (->SQLRepository ds))
(def ac (->AC (System/getenv "JWT_SECRET")))
(def scopus (->ScopusApi (System/getenv "ELSEVIER_APIKEY")))

(def app (build-api repo ac scopus))

(defn -main
  [& args]
  (run-jetty app
             {:port 3000
              :join? true}))
