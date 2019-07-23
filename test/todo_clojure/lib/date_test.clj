(ns todo-clojure.lib.date-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [todo-clojure.lib.date :as date]))

(deftest format-test
  (is (= (date/format-iso (t/date-time 2019 01 01))
         "2019-01-01T00:00:00.000Z")))