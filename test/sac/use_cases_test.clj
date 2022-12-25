(ns sac.use-cases-test
  (:require [sac.core :refer :all]
            [sac.use-cases :refer :all]
            [sac.services :refer :all]
            [sac.fake-implementations]
            [clojure.test :refer :all]))

(def repo (atom {}))
(def access-control (atom {}))
(def knowledge-base (atom {:kb [(->Article "Some title" "Cool Author" "2022-12-12" "Doi34")]}))

(deftest register-test
  (is (= "some@email.com" (register "some@email.com" "Bob" "strongpass" repo access-control)))
  (is (nil? (register "some@email.com" "Bob" "strongpass" repo access-control)))
  (is (= {"some@email.com" {:name "Bob", :password "strongpass"}} (:customers @repo))))

(deftest search-articles-test
  (is (= (:kb @knowledge-base) (search-articles ["word"] knowledge-base repo)))
  (is (= (:kb @knowledge-base) (:articles @repo))))
