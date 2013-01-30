(ns picD.server
  (:require [noir.server :as server]))

(server/load-views-ns 'picD.views)

(def app-port (Integer. (get (System/getenv) "PORT" "8080")))

; Edit these uri links with your user and password !
(defn connect-to-db []
 ;(monger.core/connect-via-uri! "mongodb://<username>:<password>@linus.mongohq.com:10003/picture-db")
 (monger.core/connect!)
 (monger.core/set-db! (monger.core/get-db "picture-db"))
)

(defn -main [& args]
  (connect-to-db)
  (server/start app-port))
