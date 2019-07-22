(ns todo-clojure.lib.utils
  (:require [clojure.pprint :as pp]))

(defn inspect [data]
  "Pretty prints a value, and returns the value for further processing."
  (do (pp/pprint data)
      data))
