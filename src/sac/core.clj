(ns sac.core
  "Domain entities and auto generated functions for SAC app

  Article is a data we need to cache for our Customer")

(defrecord Article [title author date doi])

(defrecord Customer [email name])
