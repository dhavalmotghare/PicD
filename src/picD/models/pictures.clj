(ns picD.models.pictures
  (:require [monger.collection :as monger])
  
(:import [org.bson.types ObjectId]))

(defn unidify [doc]
  (assoc doc :_id (str (:_id doc))))

(defn store-picture [url id]
  (monger/update "pictures" {:_id (ObjectId. id)} {"$set" {:imgurl url}}))

(defn all-pictures []
  (map unidify (monger/find-maps "pictures" {} )))

(defn save-picture [title author]
(monger/insert "pictures" 
    {:_id (ObjectId.)
    :title title
    :author author 
    })
  )
