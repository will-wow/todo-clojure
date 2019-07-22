(ns todo-clojure.models.todo
  (:require
   [hugsql.core :as hugsql]
   [java-time :as time]))

(hugsql/def-db-fns "sql/todo.sql")

(defn format-todo [todo]
  (update todo
          :created_at
          (fn [created_at]
            (time/format
             :iso-local-date-time
             (time/local-date-time created_at)))))