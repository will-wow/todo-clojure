(ns todo-clojure.models.todo-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-time.core :as t]
            [todo-clojure.models.todo :as todo]))

(deftest formats-date
  (is (= (todo/format-todo {:id 1
                            :title "Learn clojure"
                            :done false
                            :created_at (t/date-time 2019 01 01)})
         {:id 1
          :title "Learn clojure"
          :done false
          :created_at "2019-01-01T00:00:000Z"})))
