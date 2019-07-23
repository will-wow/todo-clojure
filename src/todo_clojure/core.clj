(ns todo-clojure.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [compojure.coercions :refer :all]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [ring.middleware.cors :refer [wrap-cors]]
            [clojure.string :as str]
            [db.core :refer [connection] :rename {connection db}]
            [todo-clojure.ring.middleware :as middleware]
            [todo-clojure.lib.date :as date]
            [todo-clojure.lib.utils :refer :all]
            [todo-clojure.models.todo :as todo :refer [format-todo]]))

(defn list-todos [req]
  {:status 200
   :body (->>
          (todo/all-todos db)
          (map format-todo))})

(defn get-todo [req]
  (let [todo (->> {:id (Integer/parseInt (:id (:params req)))}
                  (todo/get-todo db))]
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
                       {:title (:title data)
                        :created_at (date/parse-iso (:created-at data))})]
           (->>
            (todo/get-todo db id_map)
            (format-todo)))})

(defn update-todo [req]
  {:status 200
   :body (let [data (:body req)
               id_map (todo/update-todo
                       db
                       {:id (:id data)
                        :title (:title data)
                        :done (:done data)})]
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
      middleware/kebab-body
      wrap-json-body
      wrap-json-response
      middleware/kebab-body
      middleware/ignore-trailing-slash
      (wrap-cors :access-control-allow-origin [#".*"] :access-control-allow-methods [:get :put :post :delete])))
