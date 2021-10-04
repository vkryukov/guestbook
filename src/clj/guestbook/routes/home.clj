(ns guestbook.routes.home
  (:require
    [guestbook.layout :as layout]
    [guestbook.db.core :as db]
    [guestbook.middleware :as middleware]
    [ring.util.response]
    [ring.util.http-response :as response]
    [guestbook.validation :refer [validate-message]]))

(defn home-page [{:keys [flash] :as request}]
  (layout/render request "home.html"
                 (merge
                   {:messages (db/get-messages)}
                   (select-keys flash [:name :message :errors]))))

(defn about-page [request]
  (layout/render request "about.html"))

(defn save-message! [{:keys [params]}]
  (if-let [errors (validate-message params)]
    (response/bad-request {:errors errors})
    (try
      (db/save-message! params)
      (response/ok {:status :ok})
      (catch Exception e
        (response/internal-server-error
          {:errors {:server-error ["Failed to save message!"]}})))))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]
   ["/message" {:post save-message!}]
   ["/about" {:get about-page}]
   ])

