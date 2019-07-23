(ns todo-clojure.lib.date
  (:require
   [clj-time.jdbc]
   [clj-time.format :as f]))

(def iso-formatter (f/formatters :date-time))

(defn parse-iso [date-string]
  (f/parse iso-formatter date-string))

(defn format-iso [date]
  (f/unparse iso-formatter date))

