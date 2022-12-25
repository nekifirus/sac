(defproject sac "0.1.0-SNAPSHOT"
  :description "Science articles cash. Exam task to join Clojure developers team"
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [metosin/compojure-api "2.0.0-alpha31"]
                 [buddy/buddy-sign "3.4.333"]
                 [com.github.seancorfield/next.jdbc "1.3.847"]
                 [com.h2database/h2 "1.4.199"]
                 [ring/ring-jetty-adapter "1.6.3"]]
  :ring {:handler sac.application/app}
  :main sac.application
  :plugins [[lein-ring "0.12.6"]]
  :repl-options {:init-ns sac.core})
