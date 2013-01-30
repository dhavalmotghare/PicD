(defproject PicD "0.1.0-SNAPSHOT"
       :description "A simple webapp to upload and display picture"
       :dependencies [[org.clojure/clojure "1.4.0"]
                           [noir "1.3.0-beta10"]
                           [com.novemberain/monger "1.3.1"]
                           [fetch "0.1.0-alpha2"]
			   [ring/ring-core "1.1.0"]
                           [ring/ring-devel "1.1.0"]
                           [ring/ring-jetty-adapter "1.1.0"]
                           [ring-json-params "0.1.3"]
                           [clj-time "0.4.4"]
                           [compojure "0.6.4"]
                           [jayq "0.1.0-alpha3"]
                           [clj-http "0.5.5"]]
       :main picD.server)

