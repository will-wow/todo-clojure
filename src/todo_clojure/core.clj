(ns todo-clojure.core
  (:require [org.httpkit.server :as server]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.coercions :refer :all]
            [ring.middleware.defaults :refer :all]
            [clojure.pprint :as pp]
            [clojure.string :as str]
            [clojure.data.json :as json]
            [db.core :refer [connection] :rename {connection db}]
            [java-time :as time]
            [todo-clojure.models.todo :as todo]))

(defn inspect [data]
  (do (pp/pprint data)
      data))

(defn format-todo [todo]
  (update todo :created_at
          (fn [created_at] (time/format
                            :iso-local-date-time
                            (time/local-date-time created_at)))))


(defn list-todos [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (->>
          (todo/all-todos db)
          (map format-todo)
          (json/write-str))})

(defn get-todo [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (->>
          {:id (Integer/parseInt (:id (:params req)))}
          (inspect)
          (todo/get-todo db)
          (inspect)
          (format-todo)
          (json/write-str))})

(defn create-todo [req]
  {:status 201
   :headers {"Content-Type" "application/json"}
   :body (let [data (->>
                     (:body req)
                     (slurp)
                     (json/read-str))
               id_map (todo/create-todo
                       db
                       {:title (get data "title")
                        :created_at (time/local-date-time)})
               _ (inspect id_map)]

           (->>
            (todo/get-todo db id_map)
            (format-todo)
            (json/write-str)))})


(defn update-todo [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (->>
          {:id (:id (:params req))}
          (todo/get-todo db)
          (json/write-str))})


(defroutes app-routes
  (GET "/api/todos" [] list-todos)
  (GET "/api/todos/:id" [id] get-todo)
  (POST "/api/todos" [] create-todo)
  (route/not-found "Error, page not found!"))

(defn -main
  "this is our main entry point"
  [& args]
  (let [port (Integer/parseInt "3001")]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes api-defaults)
                       {:port port})
    (println (str "Running webserver at http://123.0.0.1:" port))))