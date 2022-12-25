(ns sac.fake-implementations
  "Implementations of abstractions defined in services namespace.
  Used for tests. The simplest way to use Atom for this purpose."
  (:require [sac.core :refer :all]
            [sac.services :refer :all]
            [sac.use-cases :refer [register search-articles]]))

(extend-type clojure.lang.Atom
  KnowledgeBase
  (get-articles [kb words] (:kb @kb))

  ArticlesRepository
  (save-articles [ar articles]
    (let [old-articles (:articles @ar)
          new-articles (into [] (concat old-articles articles))]
      (swap! ar assoc :articles new-articles)
      articles))
  (list-articles [ar page page-size] (:articles @ar)) ; pagination skipped here
  (count-articles [ar] (count (:articles @ar)))

  CustomerRepository
  (make-customer [cr email name password]
    (swap! cr assoc-in [:customers email] {:name name :password password}))
  (get-customer [cr email]
    (let [customer-data (get-in @cr [:customers email])]
      (when customer-data (->Customer email (:name customer-data)))))
  (password-valid? [cr email password]
    (= password (get-in @cr [:customers email :password])))

  AccessControl                         ; The most secure implementation
  (authenticate [ac email password customer-repo]
    (when (password-valid? customer-repo email password) email))
  (authorize [ac token customer-repo]
    (when (get-customer customer-repo token) token)))

