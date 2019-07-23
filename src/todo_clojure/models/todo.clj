(ns todo-clojure.models.todo
  (:require
   [hugsql.core :as hugsql]
   [todo-clojure.lib.date :as date]))

(hugsql/def-db-fns "sql/todo.sql")

(defn format-todo [todo]
  (update todo
          :created_at
          (fn [created-at]
            (date/format-iso created-at))))
