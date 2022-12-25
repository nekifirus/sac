(ns sac.scopus-api
  "KnowledgeBase implementation with api.elsevier"
  (:require [sac.services :refer :all]
            [sac.core :refer :all]
            [clojure.string :refer [join]]
            [cheshire.core :refer [parse-string]]))

(defn build-url [words api-key]
  (str "https://api.elsevier.com/content/search/scopus?query=all("
       (join " " words)
       ")&apiKey="
       api-key))

(deftype ScopusApi [api-key]
  KnowledgeBase
  (get-articles [_ words]
    (let [result (-> (build-url words api-key)
                     (slurp)
                     (parse-string)
                     (get-in ["search-results" "entry"]))
          data (take 10 result)]
      (map #(->Article (% "prism:publicationName")
                       (% "dc:creator")
                       (% "prism:coverDate")
                       (% "prism:doi")) data))))

