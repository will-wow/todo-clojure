(ns db.core
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [environ.core :refer [env]]))

(def connection
  "Map representing the database connection."
  {:dbtype (:database-type env)
   :dbname (:database-name env)
   :user (:database-username env)
   :password (:database-password env)
   :host (:database-host env)
   :port (:database-port env)})

(defn load-config
  "Constructs the configuration map needed by Ragtime to run migrations
   on the database."
  []
  {:datastore (ragtime.jdbc/sql-database connection)
   :migrations (ragtime.jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))