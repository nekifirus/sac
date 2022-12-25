(ns sac.sql-repository
  "Implementation repositories protocols with next.jdbc"
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]
            [sac.services :refer [ArticlesRepository CustomerRepository]]
            [sac.sql-queries :as q]
            [sac.core :refer [map->Article]]))

(defn build-sql-datasource [db-spec]
  (let [ds (jdbc/get-datasource db-spec)]
    (jdbc/with-options ds {:builder-fn rs/as-unqualified-lower-maps})))

(defn init-repo [ds] (jdbc/execute! ds [q/create-tables]))
(defn reset-repo [ds] (jdbc/execute! ds [q/drop-tables]))

(deftype SQLRepository [ds]
  CustomerRepository
  (make-customer [_ email name password]
    (jdbc/execute-one! ds [q/insert-customer email name password]))
  (get-customer [_ email]
    (jdbc/execute-one! ds [q/select-customer email]))
  (password-valid? [_ email password]
      (if (= password (:password (jdbc/execute-one! ds [q/select-password email])))
        true
        false))

  ArticlesRepository
  (save-articles [_ articles]
    (doall (map #(jdbc/execute! ds [q/insert-article (:title %) (:author %) (:date %) (:doi %)])
         articles))
    articles)
    
  (list-articles [_ page page-size]
    (map map->Article (jdbc/execute! ds [q/select-articles page-size (* page-size (- page 1))])))
  (count-articles [_] (first (vals (jdbc/execute-one! ds [q/count-articles])))))


