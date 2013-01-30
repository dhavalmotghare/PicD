(ns picD.server
  (:require [noir.server :as server]))

(server/load-views-ns 'picD.views)

(def app-port (Integer. (get (System/getenv) "PORT" "8080")))

; Edit these uri links with your user and password !
(defn connect-to-db []
 (monger.core/connect-via-uri! "mongodb://dhaval:4957sjn123@linus.mongohq.com:10053/sandbox-db")
 ;(monger.core/connect!)
 ;(monger.core/set-db! (monger.core/get-db "picture-db"))
)

(defn -main [& args]
  (connect-to-db)
  (server/start app-port))
