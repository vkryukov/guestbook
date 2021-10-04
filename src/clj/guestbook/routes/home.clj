(ns guestbook.routes.home
  (:require
    [guestbook.layout :as layout]
    [guestbook.messages :as msg]
    [guestbook.db.core :as db]
    [guestbook.middleware :as middleware]
    [ring.util.response]
    [ring.util.http-response :as response]
    [guestbook.validation :refer [validate-message]]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn about-page [request]
  (layout/render request "about.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/about" {:get about-page}]
   ])

