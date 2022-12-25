(ns sac.json-api
  "JSON Api namespace for SAC app"
  (:require [sac.services :refer :all]
            [sac.use-cases :refer :all]
            [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))

(s/defschema Article
  {:title s/Str
   :author s/Str
   :date s/Str
   :doi s/Str})

(s/defschema Customer
  {:email s/Str :name s/Str})


(defn build-api [repo access-control scopus]
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Science Articles Cache API"
                    :description "API for search and store science articles by keywords"}
             :tags [{:name "api", :description "Main API"}]}}}

    (context "/api" []
      :tags ["api"]

      (POST "/sign-up" []
        :return {:token String}
        :query-params [email :- String, name :- String password :- String]
        :summary "Register endpoint"
        (let [token (register email name password repo access-control)]
          (if token
            (ok {:token token})
            (unauthorized {:error "Email already registered"}))))

      (POST "/sign-in" []
        :return {:token String}
        :query-params [email :- String, password :- String]
        :summary "Authentication endpoint"
        (let [token (authenticate access-control email password repo)]
          (if token
            (ok {:token token})
            (unauthorized {:error "Not authorized"}))))

      (POST "/me" request
        :return {:me Customer}
        :header-params [{authorization :- String nil}]
        :summary "Information about customer"
        (let [headers (:headers request) ; I know about middlewares. Not today!
              token (headers "authorization")
              email (authorize access-control token repo)]
          (if email
            (ok {:me (get-customer repo email)})
            (unauthorized {:error "Not authorized"}))))

      (POST "/find" request
        :return {:articles [Article]}
        :query-params [words :- [String]]
        :header-params [{authorization :- String nil}]
        :summary "Search articles endpoint"
        (let [headers (:headers request) ; I know about middlewares. Not today!
              token (headers "authorization")
              email (authorize access-control token repo)]
          (if email
            (ok {:articles (search-articles words scopus repo)})
            (unauthorized {:error "Not authorized"}))))

      (GET "/articles" request
        :return {:articles [Article] :total Long}
        :query-params [page :- Long page-size :- Long]
        :header-params [{authorization :- String nil}]
        :summary "List cached articles endpoint"
        (let [headers (:headers request) ; I know about middlewares. Not today!
              token (headers "authorization")
              email (authorize access-control token repo)]
          (if email
            (ok {:articles (list-articles repo page page-size)
                 :total (count-articles repo)})
            (unauthorized {:error "Not authorized"})))))))

