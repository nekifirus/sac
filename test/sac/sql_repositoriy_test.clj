(ns sac.sql-repositoriy-test
  (:require [sac.sql-repository :as sut]
            [sac.services :refer :all]
            [sac.core :as d]
            [clojure.test :refer :all]))

(def ds (sut/build-sql-datasource {:dbtype "h2" :dbname "example"}))
(def repo (sut/->SQLRepository ds))

(def a1 (d/->Article "Title1" "Author1" "Date1" "Doi1"))
(def a2 (d/->Article "Title2" "Author2" "Date2" "Doi2"))
(def a3 (d/->Article "Title3" "Author3" "Date3" "Doi3"))
(def a4 (d/->Article "Title4" "Author4" "Date4" "Doi4"))
(def a5 (d/->Article "Title5" "Author5" "Date5" "Doi5"))
(def a6 (d/->Article "Title6" "Author6" "Date6" "Doi6"))
(def a7 (d/->Article "Title7" "Author7" "Date7" "Doi7"))
(def a8 (d/->Article "Title8" "Author8" "Date8" "Doi8"))
(def a9 (d/->Article "Title9" "Author9" "Date9" "Doi9"))
(def vector-articles [a1 a2 a3 a4 a5 a6 a7 a8 a9])

(defn repo-fixture [f]
  (sut/init-repo ds)
  (f)
  (sut/reset-repo ds))

(use-fixtures :each repo-fixture)

(deftest test-customer-repo
  (is (nil? (get-customer repo "email")))
  (is (true? (boolean (make-customer repo "email" "Bob" "strongpassword"))))
  (is (= {:email "email", :name "Bob"} (get-customer repo "email")))
  (is (nil? (get-customer repo "email2")))
  (is (true? (password-valid? repo "email" "strongpassword")))
  (is (false? (password-valid? repo "email2" "strongpassword"))))

(deftest test-articles-repo
  (is (= 0 (count-articles repo)))
  (is (= vector-articles (save-articles repo vector-articles)))
  (is (= 9 (count-articles repo)))
  (is (= (take 3 vector-articles) (list-articles repo 1 3)))
  (is (= (subvec vector-articles 3 6) (list-articles repo 2 3))))

    
