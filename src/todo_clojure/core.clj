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
  (let [todo (->> {:id (Integer/parseInt (:id (:params req)))}
                  (todo/get-todo db))
        _ (inspect todo)]
    (if todo

      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (->>
              todo
              (format-todo)
              (json/write-str))}

      {:status 404
       :headers {"Content-Type" "application/json"}
       :body "{}"})))

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
                        :created_at (time/local-date-time)})]
           (->>
            (todo/get-todo db id_map)
            (format-todo)
            (json/write-str)))})


(defn update-todo [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (let [data (->>
                     (:body req)
                     (slurp)
                     (json/read-str))
               id_map (todo/update-todo
                       db
                       {:id (get data "id")
                        :title (get data "title")
                        :done (get data "done")})]
           (->>
            (todo/get-todo db id_map)
            (format-todo)
            (json/write-str)))})

(defn delete-todo [req]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (do (->>
              {:id (Integer/parseInt (:id (:params req)))}
              (todo/delete-todo db))

             "{}")})


(defroutes app-routes
  (GET "/api/todos" [] list-todos)
  (GET "/api/todos/:id" [] get-todo)
  (POST "/api/todos" [] create-todo)
  (PUT "/api/todos/:id" [] update-todo)
  (DELETE "/api/todos/:id" [] delete-todo)
  (route/not-found "Error, page not found!"))

(defn -main
  "this is our main entry point"
  [& args]
  (let [port (Integer/parseInt "3001")]
    ; Run the server with Ring.defaults middleware
    (server/run-server (wrap-defaults #'app-routes api-defaults)
                       {:port port})
    (println (str "Running webserver at http://123.0.0.1:" port))))