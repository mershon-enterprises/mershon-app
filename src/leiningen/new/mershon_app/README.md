# {{ns-name}}

## Development

Open a terminal and type `lein repl` to start a Clojure REPL
(interactive prompt).

In the REPL, type

```clojure
(run)
(browser-repl)
```

The call to `(run)` starts the Figwheel server at port 3449, which takes care of
live reloading ClojureScript code and CSS. Figwheel's server will also act as
your app server, so requests are correctly forwarded to the http-handler you
define.

Running `(browser-repl)` starts the Figwheel ClojureScript REPL. Evaluating
expressions here will only work once you've loaded the page, so the browser can
connect to Figwheel.

When you see the line `Successfully compiled "resources/public/app.js" in 21.36
seconds.`, you're ready to go. Browse to `http://localhost:3449` and enjoy.

**Attention: It is not needed to run `lein figwheel` separately. Instead we
launch Figwheel directly from the REPL**

## Playing with state

In the REPL, type

```
(in-ns '{{ns-name}}.state.app-state)
(swap! state assoc :text "Interactivity FTW")
```

## Devcards

This project incorporates the wonderous
[devcards](https://github.com/bhauman/devcards) library for quickly prototyping
new UI components. To develop new widgets or explore how existing ones behave,
use

```shell
lein figwheel devcards
```

All available "cards"

## Testing

To run the Clojure tests, use

``` shell
lein test
```

To run the Clojurescript tests you use [doo](https://github.com/bensu/doo). This
can run your tests against a variety of JavaScript implementations, but in the
browser and "headless". For example, to test with PhantomJS, use

``` shell
lein doo phantom
```

## License

Copyright © 2017 mershon-app

Distributed under the GNU General Public License either version 3.0 or (at
your option) any later version.
