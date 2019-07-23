(ns todo-clojure.ring.middleware
  (:require
   [todo-clojure.lib.utils :refer :all]
   [camel-snake-kebab.core :as csk]
   [camel-snake-kebab.extras :as cske]))

(defn ignore-trailing-slash
  "Modifies the request uri before calling the handler.
  Removes a single trailing slash from the end of the uri if present.

  Useful for handling optional trailing slashes until Compojure's route matching syntax supports regex.
  Adapted from http://stackoverflow.com/questions/8380468/compojure-regex-for-matching-a-trailing-slash"
  [handler]
  (fn [request]
    (let [uri (:uri request)]
      (handler (assoc request :uri (if (and (not (= "/" uri))
                                            (.endsWith uri "/"))
                                     (subs uri 0 (dec (count uri)))
                                     uri))))))

(defn kebab-body
  "kebab-case the json body"
  [handler]
  (fn [request]
    (let [body (:body request)]
      (handler (assoc request :body (cske/transform-keys csk/->kebab-case-keyword body))))))