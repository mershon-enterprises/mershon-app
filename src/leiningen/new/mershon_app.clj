(ns leiningen.new.mershon-app
  (:use [leiningen.new.templates :only [renderer name-to-path sanitize-ns ->files]]))

(def render (renderer "mershon-app"))

(defn mershon-app
  [name]
  (let [data {:name name
              :ns-name (sanitize-ns name)
              :sanitized (name-to-path name)}]
    (->files data
             ["dev/user.clj" (render "dev/user.clj" data)]
             [".cljfmt.edn" (render ".cljfmt.edn" data)]
             ["Procfile" (render "Procfile" data)]
             ["README.md" (render "README.md" data)]
             ["project.clj" (render "project.clj" data)]
             ["system.properties" (render "system.properties")]
             ["resources/changelog.xml" (render "resources/changelog.xml")]
             ["resources/changelogs/.git-keep" (render "resources/changelogs/.git-keep")]
             ["resources/log4j.properties" (render "resources/log4j.properties" data)]
             ["resources/public/cards.html" (render "resources/public/cards.html" data)]
             ["resources/public/css/style.css" (render "resources/public/css/style.css")]
             ["resources/public/favicon.ico" (render "resources/public/favicon.ico")]
             ["resources/public/images/nav-logo.png" (render "resources/public/images/nav-logo.png")]
             ["resources/public/index.html" (render "resources/public/index.html" data)]
             ["resources/queries/.git-keep" (render "resources/queries/.git-keep")]
             ["src/clj/patchwork.clj" (render "src/clj/patchwork.clj" data)]
             ["src/clj/{{sanitized}}/schema.clj" (render "src/clj/mershon_app/schema.clj" data)]
             ["src/clj/{{sanitized}}/server.clj" (render "src/clj/mershon_app/server.clj" data)]
             ["src/cljc/{{sanitized}}/common.cljc" (render "src/cljc/mershon_app/common.cljc" data)]
             ["src/cljc/{{sanitized}}/spec/user.cljc" (render "src/cljc/mershon_app/spec/user.cljc" data)]
             ["src/cljs/{{sanitized}}/components/login.cljs" (render "src/cljs/mershon_app/components/login.cljs" data)]
             ["src/cljs/{{sanitized}}/components/navbar.cljs" (render "src/cljs/mershon_app/components/navbar.cljs" data)]
             ["src/cljs/{{sanitized}}/components/shared.cljs" (render "src/cljs/mershon_app/components/shared.cljs" data)]
             ["src/cljs/{{sanitized}}/config.cljs" (render "src/cljs/mershon_app/config.cljs" data)]
             ["src/cljs/{{sanitized}}/core.cljs" (render "src/cljs/mershon_app/core.cljs" data)]
             ["src/cljs/{{sanitized}}/helpers/login.cljs" (render "src/cljs/mershon_app/helpers/login.cljs" data)]
             ["src/cljs/{{sanitized}}/pages/login.cljs" (render "src/cljs/mershon_app/pages/login.cljs" data)]
             ["src/cljs/{{sanitized}}/routes.cljs" (render "src/cljs/mershon_app/routes.cljs" data)]
             ["src/cljs/{{sanitized}}/state/app_state.cljs" (render "src/cljs/mershon_app/state/app_state.cljs" data)]
             ["test/clj/{{sanitized}}/example_test.clj" (render "test/clj/mershon_app/example_test.clj" data)]
             ["test/cljc/{{sanitized}}/common_test.cljc" (render "test/cljc/mershon_app/common_test.cljc" data)])))
