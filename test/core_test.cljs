(ns core-test
  (:require
   [clojure.test :refer [deftest is]]))

(deftest my-first-test
  (is (= 1 2)))

(deftest basic-test
  (is (= {:foo 1} {:foo 1}) "at least one that passes"))

(deftest output-test
  (println "this is on stdout")

  (is (= {:foo 1} {:foo 4}) "oops"))

(deftest exception-in-is-test
  (is
   (throw (js/Error. "Inside assertion"))))

(deftest exception-outside-is-test
  (throw (js/Error. "outside assertion")))

(deftest ^:kaocha/skip skip-test
  (println "this test does not run.")
  (is false))
