(ns todo-clojure.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.coercions :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cors :refer [wrap-cors]]
            [clojure.string :as str]
            [db.core :refer [connection] :rename {connection db}]
            [java-time :as time]
            [todo-clojure.ring.trailing-slash-middlware :refer [ignore-trailing-slash]]
            [todo-clojure.lib.utils :refer :all]
            [todo-clojure.models.todo :as todo :refer [format-todo]]))

(defn list-todos [req]
  {:status 200
   :body (->>
          (todo/all-todos db)
          (inspect)
          (map format-todo))})

(defn get-todo [req]
  (let [todo (->> {:id (Integer/parseInt (:id (:params req)))}
                  (todo/get-todo db))
        _ (inspect todo)]
    (if todo
      {:status 200
       :body (format-todo todo)}

      {:status 404
       :body "{}"})))

(defn create-todo [req]
  {:status 201
   :body (let [data (:body req)
               id_map (todo/create-todo
                       db
                       {:title (get data "title")
                        :created_at (time/local-date-time)})]
           (->>
            (todo/get-todo db id_map)
            (format-todo)))})

(defn update-todo [req]
  {:status 200
   :body (let [data (:body req)
               id_map (todo/update-todo
                       db
                       {:id (get data "id")
                        :title (get data "title")
                        :done (get data "done")})]
           (->>
            (todo/get-todo db id_map)
            (format-todo)))})

(defn delete-todo [req]
  {:status 204
   :body (do (->>
              {:id (Integer/parseInt (:id (:params req)))}
              (todo/delete-todo db))

             "{}")})

(defroutes app-routes
  (GET    "/todos" [] list-todos)
  (GET    "/todos/:id" [] get-todo)
  (POST   "/todos" [] create-todo)
  (PUT    "/todos/:id" [] update-todo)
  (DELETE "/todos/:id" [] delete-todo)
  (route/not-found "Error, page not found!"))

(def app
  (-> app-routes
      wrap-params
      wrap-json-body
      wrap-json-response
      ignore-trailing-slash
      (wrap-cors :access-control-allow-origin [#".*"] :access-control-allow-methods [:get :put :post :delete])))
