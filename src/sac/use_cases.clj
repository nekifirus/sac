(ns sac.use-cases
  "We have only two processes changes data. Application will have
  another functions. But CQRS is the best"
  (:require [sac.services :refer :all]))

(defn register
  "Stores a new Customer in a repository and authenticate him"
  [email name password customer-repo access-control]
  (when (nil? (get-customer customer-repo email))
    (make-customer customer-repo email name password)
    (authenticate access-control email password customer-repo)))

(defn search-articles
  "Search articles in an external knowledge base and store them into an internal storage"
  [words knowledge-base articles-repo]
  (->> words
       (get-articles knowledge-base)
       (save-articles articles-repo)))
