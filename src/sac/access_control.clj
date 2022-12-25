(ns sac.access-control
  "Simple implementation of AccessControl abstraction with JWT"
  (:require [sac.services :as s]
            [buddy.sign.jwt :as jwt]))

(deftype AC [secret]
  s/AccessControl
  (authenticate [ac email password repo]
    (when (s/password-valid? repo email password)
      (jwt/sign {:sub email} secret)))
  (authorize [ac token repo]
    (try
        (:sub (jwt/unsign token secret))
        (catch Exception e nil))))

