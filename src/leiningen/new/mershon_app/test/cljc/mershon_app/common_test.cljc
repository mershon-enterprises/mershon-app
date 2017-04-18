(ns {{ns-name}}.common-test
  #? (:cljs (:require-macros [cljs.test :refer (is deftest testing)]))
  (:require [{{ns-name}}.common :as sut]
            #?(:clj [clojure.test :refer :all]
               :cljs [cljs.test])))
