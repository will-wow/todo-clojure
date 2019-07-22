(defproject todo-clojure "0.1.0-SNAPSHOT"
  :description "A simple todo API to mess with clojure"

  :dependencies [[org.clojure/clojure "1.10.1"]
                 ; Compojure - A basic routing library
                 [compojure "1.6.1"]
                 ; Ring
                 [ring/ring-core "1.7.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [lein-ring "0.12.5"] ; Reload code
                 [ring-cors "0.1.13"]
                 [ring/ring-json "0.4.0"]
                 [ring/ring-mock "0.4.0"]
                 ; Clojure data.JSON library
                 [org.clojure/data.json "0.2.6"]
                 [environ "1.1.0"]
                 [clojure.java-time "0.3.2"]
                 [ragtime "0.8.0"]
                 [com.layerware/hugsql "0.4.9"]
                 [org.postgresql/postgresql "42.2.2"]]

  :plugins [[lein-environ "1.1.0"]
            [lein-ring "0.12.5"]]

  :aliases {"migrate"  ["run" "-m" "db.core/migrate"]
            "rollback" ["run" "-m" "db.core/rollback"]}

  :ring {:handler todo-clojure.core/app
         :port 3001
         :nrepl {:start? true
                 :port 9998}}

  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main ^:skip-aot todo-clojure.core
  :target-path "target/%s")
