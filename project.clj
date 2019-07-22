(defproject todo-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; Compojure - A basic routing library
                 [compojure "1.6.1"]
                 ; Our Http library for client/server
                 [http-kit "2.3.0"]
                 ; Ring defaults - for query params etc
                 [ring/ring-defaults "0.3.2"]
                 ; Clojure data.JSON library
                 [org.clojure/data.json "0.2.6"]
                 [environ "1.1.0"]
                 [clojure.java-time "0.3.2"]
                 [ragtime "0.8.0"]
                 [com.layerware/hugsql "0.4.9"]
                 [org.postgresql/postgresql "42.2.2"]]

  :plugins [[lein-environ "1.1.0"]]

  :aliases {"migrate"  ["run" "-m" "db.core/migrate"]
            "rollback" ["run" "-m" "db.core/rollback"]}

  :profiles
  {:dev {:env {:environment "development"
               :port "8080"
               :database-type "postgresql"
               :database-name "app"
               :database-username "postgres"
               :database-password "postgres"
               :database-host "localhost"
               :database-port "5424"}}
   :test {:env {:environment "test"}}
   :prod {:env {:environment "production"
                :aot :all}}
   :uberjar {:aot :all}}

  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot todo-clojure.core
  :target-path "target/%s")
