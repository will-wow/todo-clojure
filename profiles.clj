{:dev {:env {:environment "development"
             :port "8080"
             :database-type "postgresql"
             :database-name "app"
             :database-username "postgres"
             :database-password "postgres"
             :database-host "localhost"
             :database-port "5434"}}
 :test {:env {:environment "test"}}
 :prod {:env {:environment "production"
              :aot :all}}}
