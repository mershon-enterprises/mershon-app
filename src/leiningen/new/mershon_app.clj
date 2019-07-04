(ns leiningen.new.mershon-app
  (:use [leiningen.new.templates :only [renderer name-to-path sanitize-ns ->files raw-resourcer]]))

(def render (renderer "mershon-app"))
(def copy (raw-resourcer "mershon-app"))

(defn mershon-app
  [name]
  (let [data {:name name
              :ns-name (sanitize-ns name)
              :sanitized (name-to-path name)}]
    (->files data
             ["dev/user.clj" (render "dev/user.clj" data)]
             [".cljfmt.edn" (render "cljfmt.edn" data)]
             [".gitignore" (render "gitignore" data)]
             ["Procfile" (render "Procfile" data)]
             ["README.md" (render "README.md" data)]
             ["project.clj" (render "project.clj" data)]
             ["system.properties" (render "system.properties")]
             ["resources/changelog.xml" (render "resources/changelog.xml")]
             ["resources/changelogs/.git-keep" (copy "resources/changelogs/git-keep")]
             ["resources/log4j.properties" (render "resources/log4j.properties" data)]
             ["resources/public/cards.html" (render "resources/public/cards.html" data)]
             ["resources/public/css/style.css" (render "resources/public/css/style.css")]
             ["resources/public/favicon.ico" (copy "resources/public/favicon.ico")]
             ["resources/public/images/nav-logo.png" (copy "resources/public/images/nav-logo.png")]
             ["resources/private/index.html" (render "resources/private/index.html" data)]
             ["resources/queries/.git-keep" (copy "resources/queries/git-keep")]
             ["src/clj/patchwork/core.clj" (render "src/clj/patchwork/core.clj" data)]
             ["src/clj/{{sanitized}}/helpers.clj" (render "src/clj/mershon_app/helpers.clj" data)]
             ["src/clj/{{sanitized}}/schema.clj" (render "src/clj/mershon_app/schema.clj" data)]
             ["src/clj/{{sanitized}}/server.clj" (render "src/clj/mershon_app/server.clj" data)]
             ["src/clj/{{sanitized}}/session.clj" (render "src/clj/mershon_app/session.clj" data)]
             ["src/clj/{{sanitized}}/token.clj" (render "src/clj/mershon_app/token.clj" data)]
             ["src/clj/{{sanitized}}/routes/opengraph.clj" (render "src/clj/mershon_app/routes/opengraph.clj" data)]
             ["src/clj/{{sanitized}}/routes/version.clj" (render "src/clj/mershon_app/routes/version.clj" data)]
             ["src/cljc/{{sanitized}}/common.cljc" (render "src/cljc/mershon_app/common.cljc" data)]
             ["src/cljc/{{sanitized}}/spec/user.cljc" (render "src/cljc/mershon_app/spec/user.cljc" data)]
             ["src/cljs/{{sanitized}}/components/login.cljs" (render "src/cljs/mershon_app/components/login.cljs" data)]
             ["src/cljs/{{sanitized}}/components/navbar.cljs" (render "src/cljs/mershon_app/components/navbar.cljs" data)]
             ["src/cljs/{{sanitized}}/components/shared.cljs" (render "src/cljs/mershon_app/components/shared.cljs" data)]
             ["src/cljs/{{sanitized}}/config.cljs" (render "src/cljs/mershon_app/config.cljs" data)]
             ["src/cljs/{{sanitized}}/core.cljs" (render "src/cljs/mershon_app/core.cljs" data)]
             ["src/cljs/{{sanitized}}/helpers/login.cljs" (render "src/cljs/mershon_app/helpers/login.cljs" data)]
             ["src/cljs/{{sanitized}}/helpers/shared.cljs" (render "src/cljs/mershon_app/helpers/shared.cljs" data)]
             ["src/cljs/{{sanitized}}/pages/login.cljs" (render "src/cljs/mershon_app/pages/login.cljs" data)]
             ["src/cljs/{{sanitized}}/routes.cljs" (render "src/cljs/mershon_app/routes.cljs" data)]
             ["src/cljs/{{sanitized}}/state/app_state.cljs" (render "src/cljs/mershon_app/state/app_state.cljs" data)]
             ["test/clj/{{sanitized}}/example_test.clj" (render "test/clj/mershon_app/example_test.clj" data)]
             ["test/cljc/{{sanitized}}/common_test.cljc" (render "test/cljc/mershon_app/common_test.cljc" data)])))
