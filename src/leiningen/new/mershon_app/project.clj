(defproject {{ns-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "GNU General Public License"
            :url "https://www.gnu.org/licenses/gpl-3.0.en.html"}

  :dependencies [[org.clojure/clojure "1.9.0-alpha15" :exclusions [core.async]]
                 [org.clojure/clojurescript "1.9.494" :scope "provided"]
                 [org.clojure/core.async "0.3.442"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [bk/ring-gzip "0.1.1"]
                 [ring.middleware.logger "0.5.0" :exclusions [onelog]]
                 [onelog "0.5.0"]
                 [compojure "1.5.0"]
                 [environ "1.1.0"]
                 [org.omcljs/om "1.0.0-alpha36"]
                 [org.tobereplaced/mapply "1.0.0"]
                 [ring/ring-json "0.4.0"]
                 [ring-json-response "0.2.0"]
                 [org.clojure/data.json "0.2.6"]

                 ; logging
                 [org.slf4j/slf4j-api "1.7.5"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]

                 ; db versioning
                 [clj-dbcp      "0.8.1"] ; to create connection-pooling DataSource
                 [clj-liquibase "0.6.0"] ; for this library

                 ; db connection
                 [yesql "0.5.2"]
                 [org.postgresql/postgresql "9.4-1201-jdbc41"]

                 ;ClojureScript related dependencies
                 [racehub/om-bootstrap "0.6.1"]
                 [secretary "1.2.3"]
                 [cljs-ajax "0.5.8"]
                 [clj-http "2.0.0"]
                 [funcool/promesa "1.5.0"]

                 [devcards "0.2.2"]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-environ "1.0.3"]
            [lein-test-out "0.3.1"]
            [lein-cljfmt "0.5.6"] ]

  :min-lein-version "2.6.1"

  :source-paths ["src/clj" "src/cljs" "src/cljc"]

  :test-paths ["test/clj" "test/cljc"]

  :clean-targets ^{:protect false} [:target-path :compile-path "resources/public/js"]

  :uberjar-name "{{ns-name}}.jar"

  ;; Use `lein run` if you just want to start a HTTP server, without figwheel
  :main {{ns-name}}.server

  ;; nREPL by default starts in the :main namespace, we want to start in `user`
  ;; because that's where our development helper functions like (run) and
  ;; (browser-repl) live.
  :repl-options {:init-ns user}

  :cljfmt {:indents ~(clojure.edn/read-string (slurp ".cljfmt.edn"))}

  :prep-tasks [["cljfmt" "fix"] "javac" "compile"]

  :cljsbuild {:builds
              [{:id "app"
                :source-paths ["src/cljs" "src/cljc"]

                :figwheel true
                ;; Alternatively, you can configure a function to run every time figwheel reloads.
                ;; :figwheel {:on-jsload "{{ns-name}}.core/on-figwheel-reload"}

                :compiler {:main {{ns-name}}.core
                           :asset-path "/js/compiled/out"
                           :output-to "resources/public/js/compiled/{{sanitized}}.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}

               {:id "devcards"
                :source-paths ["src/cljs" "src/cljc"]
                :figwheel {:devcards true
                           :open-urls ["http://localhost:3449/cards.html"]}
                :compiler {:main {{ns-name}}.core
                           :asset-path "js/compiled/devcards_out"
                           :output-to "resources/public/js/compiled/{{sanitized}}_devcards.js"
                           :output-dir "resources/public/js/compiled/devcards_out"}}

               {:id "test"
                :source-paths ["src/cljs" "test/cljs" "src/cljc" "test/cljc"]
                :compiler {:output-to "resources/public/js/compiled/testable.js"
                           :main {{ns-name}}.test-runner
                           :optimizations :none}}

               {:id "min"
                :source-paths ["src/cljs" "src/cljc"]
                :jar true
                :compiler {:main {{ns-name}}.core
                           :output-to "resources/public/js/compiled/{{sanitized}}.js"
                           :output-dir "target"
                           :source-map-timestamp true
                           :optimizations :whitespace
                           :pretty-print false}}]}

  ;; When running figwheel from nREPL, figwheel will read this configuration
  ;; stanza, but it will read it without passing through leiningen's profile
  ;; merging. So don't put a :figwheel section under the :dev profile, it will
  ;; not be picked up, instead configure figwheel here on the top level.

  :figwheel {;; :http-server-root "public"       ;; serve static assets from resources/public/
             :server-port 3449
             ;; :server-ip "127.0.0.1"           ;; default
             :css-dirs ["resources/public/css"]  ;; watch and update CSS

             ;; Instead of booting a separate server on its own port, we embed
             ;; the server ring handler inside figwheel's http-kit server, so
             ;; assets and API endpoints can all be accessed on the same host
             ;; and port. If you prefer a separate server process then take this
             ;; out and start the server with `lein run`.
             :ring-handler user/http-handler

             ;; Start an nREPL server into the running figwheel process. We
             ;; don't do this, instead we do the opposite, running figwheel from
             ;; an nREPL process, see
             ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
             ;; :nrepl-port 7888

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             :server-logfile "log/figwheel.log"}

  :doo {:build "test"}

  :profiles {:dev
             {:dependencies [[figwheel "0.5.9"]
                             [figwheel-sidecar "0.5.9"]
                             [com.cemerick/piggieback "0.2.1"]
                             [org.clojure/tools.nrepl "0.2.12"]]

              :env {:hostproto    "http"
                    :hostname     "localhost"
                    :rds-hostname "localhost"
                    :rds-port     "5432"
                    :rds-db-name  "{{sanitized}}"
                    :rds-username "postgres"
                    :rds-password "password"

                    :patchwork-base-url            "https://patchwork.mershonenterprises.com"
                    :patchwork-application         "APPLICATION"
                    :patchwork-token               "TOKEN"
                    :patchwork-logger-platform-uid ""}

              :plugins [[lein-figwheel "0.5.9"]
                        [lein-doo "0.1.6"]
                        [lein-cloverage "1.0.9"]
                        [com.jakemccrary/lein-test-refresh "0.19.0"]]

              :source-paths ["dev"]
              :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}

             :test {:dependencies [[ring-mock "0.1.5"]]
                    :env {:hostproto "https"}}

             :uberjar
             {:source-paths ^:replace ["src/clj" "src/cljc"]
              :prep-tasks ["compile" ["cljsbuild" "once" "min"]]
              :hooks []
              :omit-source true
              :aot :all}})
