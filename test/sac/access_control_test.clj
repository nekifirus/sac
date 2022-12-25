(ns sac.access-control-test
  (:require [sac.access-control :refer :all]
            [sac.services :refer [authenticate authorize]]
            [sac.fake-implementations]
            [clojure.test :refer :all]))

(def ac (->AC "somestrongsecret"))
(def repo (atom {:customers {"some@email.com" {:password "strongpassword"}}}))

(deftest test-access-control
  (is (nil? (authenticate ac "some2@email.com" "strongpassword" repo)))
  (is (true? (boolean (authenticate ac "some@email.com" "strongpassword" repo))))
  (is (= "some@email.com" (let [token (authenticate ac "some@email.com" "strongpassword" repo)]
                            (authorize ac token repo)))))



