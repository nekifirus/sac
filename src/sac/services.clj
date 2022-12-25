(ns sac.services
  "Abstractions needed by use-cases")

(defprotocol KnowledgeBase
  (get-articles [knowledge-base words] "Returns list articles from knowledge base"))

(defprotocol ArticlesRepository
  (save-articles [repo articles] "Stores articles in an internal storage")
  (list-articles [repo page page-size] "Returns list articles from an internal storage with pagination")
  (count-articles [repo] "Returns count of articles in an internal storage"))

(defprotocol CustomerRepository
  (make-customer [repo email name password] "Creates new customer record")
  (get-customer [repo email] "Returns a customer if exists")
  (password-valid? [repo email password] "Verifies customer's password"))

(defprotocol AccessControl
  (authenticate [ac email password customer-repo] "Authenticates customer, returns access token")
  (authorize [ac token customer-repo] "Returns customer's id from token"))
