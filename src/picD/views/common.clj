(ns picD.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page :only [include-css include-js html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "PicD"]
               (include-css "/css/reset.css")
               (include-css "/css/bootstrap.css")
               (include-css "/css/app.css")]
              [:body
               [:div.container
                [:div#flash-message
                  [:p "Processing"
                    [:br]
                    [:img {:src "/img/ajax-loader.gif"}]]
                  ]
                [:h1 "Gallery"]
                [:hr]
                content]
                (include-js "/js/jquery.min.js")
                (include-js "/js/filepicker.js")
                (include-js "/js/main.js")
                ]))
