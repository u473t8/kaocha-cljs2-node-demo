(ns kaocha-hooks
  (:require
   [clojure.java.shell :as sh]
   [shadow.cljs.devtools.api :as shadow-api]
   [shadow.cljs.devtools.server.runtime :as shadow-runtime]
   [shadow.cljs.devtools.server :as shadow-server]))

(defn spawn
  "Start a process, connecting its stdout/stderr to the parent so we see what's
  going on. Returns the Process object so you can call .pid, .destroy,
  .destroyForcibly."
  [& args]
  (.start (ProcessBuilder. args)))

(defn ensure-funnel [test]
  ;; If funnel is already running then this is a no-op
  (sh/sh "bin/funnel" "--daemonize")
  test)

(defn ensure-shadow-instance [test]
  (when (nil? @shadow-runtime/instance-ref)
    (shadow-server/start!)
    (loop []
      (Thread/sleep 250)
      (when (nil? @shadow-runtime/instance-ref)
        (recur))))
  test)

(defn shadow-dev-build [test]
  (shadow-api/compile (:shadow/build test))
  test)

(defn start-node [test]
  (let [node (spawn "node" (:node/main-js test))]
    (update test :kaocha.hooks/post-test (fnil conj []) (fn [test _] (.destroy node) test))))

(defn pre-load-test [test _test-plan]
  (if (= (:kaocha.testable/type test) :kaocha.type/cljs2)
    (-> test
        (ensure-funnel)
        (ensure-shadow-instance)
        (shadow-dev-build)
        (start-node))
    test))


