This is a minimal setup required to run ClojureScript tests in a Shadow-CLJS project using the Kaocha test runner with the Node.js runtime.

To run the demo tests, follow these steps:

```bash
chmod +x bin/funnel

npm install

clojure -M:test -m kaocha.runner node
```

Test focusing works as usual:
```bash
clojure -M:test -m kaocha.runner node --focus core-test/basic-test
```
