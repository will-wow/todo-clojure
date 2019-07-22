(ns todo-clojure.models.todo
  (:require [hugsql.core :as hugsql]))

(hugsql/def-db-fns "sql/todo.sql")
