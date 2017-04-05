(defproject codescan "0.1.0-SNAPSHOT"
  :description "Jemurai Codescan: Check code for common issues."
  :url "http://jemurai.com"
  :license {:name "Copyright Jemurai, 2014 and 2015."
            :url "http://jemurai.com/codescan.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
				 [org.clojure/tools.cli "0.3.1"]]
  :main ^:skip-aot jemurai.codescan.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
