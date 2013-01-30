(ns picD.views.welcome
  (:use [noir.core :only [defpage defpartial render]]
        [hiccup.form]
        [clojure.java.io]
        [ring.middleware.params]
        [ring.middleware.multipart-params]
        [noir.fetch.remotes]
        [compojure.core]
        [monger.gridfs :only [store-file make-input-file filename content-type metadata]])  
  (:require [picD.views.common :as common]
            [noir.response :as response]
            [picD.models.pictures :as mpictures]
            [monger.collection :as monger]
            [monger.core :as mg]
            [monger.gridfs :as gfs])
  (:import (org.bson.types ObjectId)
           [com.mongodb MongoOptions ServerAddress]
           [com.mongodb Mongo DB DBCollection DBObject]
           [java.io File])
)

(defn store [filepath,name,frmt]
  (store-file (make-input-file filepath)
  (filename name)
  (metadata {:format frmt})
  (content-type "image/jpeg")))

(defn findfile[name]
  (gfs/find {:filename name}))

(defn findfileInfo[name]
  (gfs/find-maps {:filename name}))

(defn noOfDocuments []
   (count (gfs/all-files)))

(defn upload-file [file]
  (let [file-name (file :filename)
        size (file :size)
        actual-file (file :tempfile)]
    (do
      (println (str file-name))
      (copy actual-file (File. (format "%s" file-name)))
      (store file-name file-name "jpeg")
     )))  

;Form to send title and author for the image
(defpartial add-picture-form []
          [:p (text-field {:placeholder "title" :id "title"} "title")]
          [:p (text-field {:placeholder "author" :id "author"} "author")]
          (hidden-field {:id "picture-img" :picture-img-url "none"} "") ; This field needed for Submit it contains picture url
          [:div#bimg ] ;Handler for picture that will be inserted here after succesful upload
 	  [:P (file-upload :file)]
	  [:p (submit-button {:id "add-picture-btn"} "Submit")]
          [:br]
          [:br ] )
  
;[Template] Picture box with title and author  
(defpartial picture-box [{:keys [ _id title author imgurl]}]
  [:div.row 
    [:div.span2 {:id "img-box"} 
      [:p {:id (str "picture-img-"_id)}
        (if imgurl
		[:img {:src (str "./image?name="  imgurl )}]
        )
      ]
      [:hr]    
    ]
    [:div.span6
      [:p [:span.lbl "Author: "]
        [:span author]]
      [:p [:span.lbl "Title: " ]
        [:span title]]
    ]
  ])

;Pass data to template
(defpartial pictures-list []
  (map picture-box (mpictures/all-pictures)))

;Save our data in db
(defn store-picture [author title iurl]
  (println "store pictures was called")
  (if (monger/insert "pictures"
        {:_id (ObjectId.)
        :title title
        :author author
        :imgurl iurl })
    (pictures-list))
  )

(defn store-image [url pid]
  (mpictures/store-picture url pid)
  "ok" ; Need to return something readble here - for example string
  )

;Main page 
(defpage "/" []
         (common/layout
           ;[:hr]
           [:h3 "Upload a Photo"]
           (form-to {:enctype "multipart/form-data"} 
		[:post "/submitdata"]
           (add-picture-form))
           [:hr]
           [:div#pictures-list(pictures-list)]
          ))

(defpage [:post "/submitdata"] {:keys [author title file]}
   (try
      (upload-file file)
      (let [file-name (file :filename)]
           (store-picture author title file-name))
      (render "/")
      (catch Exception ex
        (println (str (.getMessage ex))))
   )
)

(defn getbyteArray [name]
           (println ( str name ))
           (-> (gfs/find-one name) (.writeTo (str "./temp/" name)))
           (with-open [input (new java.io.FileInputStream (str "./temp/" name)) 
           output (new java.io.ByteArrayOutputStream)]
           (clojure.java.io/copy input output)
           (.toByteArray output)))

(defn handleImageRequest [name]
     (response/content-type "image/jpeg" (new java.io.ByteArrayInputStream (getbyteArray name))))

(defpage "/image" {:keys [name]} (handleImageRequest name))

