(ns todo-clojure.core-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [todo-clojure.models.todo :as todo]
            [clj-time.core :as t]
            [clojure.data.json :as json]
            [todo-clojure.core :refer [app]]))

(deftest index-test
  (with-redefs [todo/all-todos (fn [_] (list {:id 1
                                              :title "Learn clojure"
                                              :done false
                                              :created_at (t/date-time 2019 01 01)}))]

    (is (= (app (mock/request :get "/todos"))
           {:status 200
            :headers {"Content-Type" "application/json; charset=utf-8"}
            :body (json/write-str [{:id 1
                                    :title "Learn clojure"
                                    :done false
                                    :created_at "2019-01-01T00:00:000Z"}])}))))

