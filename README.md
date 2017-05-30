# Mershon-App Leiningen Template

### Installation

Clone the repository and change into directory
```sh
$ git clone https://github.com/mershon-enterprises/mershon-app.git
$ cd mershon-app/
```
In the root directory of the `mershon-app` project, run `lein-install`
```sh
$ lein install
```

Edit your `profiles.clj` file
```sh
vim $HOME/.lein/profiles.clj
```
Add `mershon-app/lein-template "0.0.1"` as a dependency in your `lein/profiles.clj`
__Example__
```clj
{:user {:plugins [[cider/cider-nrepl "0.14.0"]
                  [mershon-app/lein-template "0.0.1"]]}}
```

Restart your terminal session and run `lein new mershon-app {{project-name}}`
