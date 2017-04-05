(ns jemurai.codescan.core
 (:require 
  [clojure.string :as string]
  [clojure.tools.cli :refer [parse-opts]])
 (use jemurai.codescan.files) 
 (use jemurai.codescan.checks.secrets)
 (use jemurai.codescan.checks.unescaped)
 (use jemurai.codescan.checks.injection)
 (:import (java.io File))
 (:gen-class))

(def cli-options
  ;; An option with a required argument
  [
   ;; A non-idempotent option
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [m k _] (update-in m [k] inc))]
   ;; A boolean option defaulting to nil
   ["-h" "--help"]])

(defn usage [options-summary]
  (->> ["This is a program written to analyze code."
        ""
        "Usage: codescan [options] directory"
        ""
        "Options:"
        options-summary
        ""
        "Directory:"
        "  The root of your code project."]
       (string/join \newline)))

(defn error-msg [errors]
  (str "The following errors occurred while parsing your command:\n\n"
       (string/join \newline errors)))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(defn -main [& args]
  (time 
   (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
     ;; Handle help and error conditions
     (cond
      (:help options) (exit 0 (usage summary))
      (not= (count arguments) 1) (exit 1 (usage summary))
      errors (exit 1 (error-msg errors)))
     
     (println "Common issues ... ")
     ;(findissues (first arguments))
     
     (println "Secrets...")
     (find-secrets (first arguments))
     (find-secret-files (first arguments))
     
     (println "Unescaped...")
     (find-unescaped-html (first arguments))
     
     (println "Injection...")
     (find-injection (first arguments))
     
     (println "LOC ... ")
     (loc (first arguments))   
     
     ;; Execute program with options
     (println "Working on stats")
     (filestats (first arguments))
     

     )
   )
  )

