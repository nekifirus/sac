(ns sac.application
  "Application entrypoint
  We can simple change concrete implementations here"
  (:require [sac.scopus-api :refer [->ScopusApi]]
            [sac.access-control :refer [->AC]]
            [sac.json-api :refer [build-api]]
            [sac.sql-repository :refer [build-sql-datasource init-repo ->SQLRepository]]))

(def ds (build-sql-datasource {:dbtype "h2" :dbname "sac"}))
(init-repo ds)
(def repo (->SQLRepository ds))
(def ac (->AC (System/getenv "JWT_SECRET")))
(def scopus (->ScopusApi (System/getenv "ELSEVIER_APIKEY")))


(def app (build-api repo ac scopus))

