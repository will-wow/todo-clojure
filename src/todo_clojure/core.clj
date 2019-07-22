(ns todo-clojure.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]))

(defn simple-body-page [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn request-example [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (do (pp/pprint req)
             (->>
              req
              (:cookies)
              (json/write-str)
              (str "Request object: ")))})

(defn hello-name [req]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (do
           (pp/pprint req)
           (str "hello " (:name (:params req))))})

(defroutes app-routes
  (GET "/" [] simple-body-page)
  (GET "/request" [] request-example)
  (GET "/hello" [] hello-name)
  (route/not-found "Error, page not found!"))

(defn -main
  "this is our main entry point"
  [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes site-defaults)
                       {:port port})
    (println (str "Running webserver at http://123.0.0.1:" port))))