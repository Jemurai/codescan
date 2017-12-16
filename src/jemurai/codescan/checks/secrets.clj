; Quickly identify secrets.
(ns jemurai.codescan.checks.secrets
  (:require 
   [clojure.string :as string]
  )
  (use jemurai.codescan.report)
  (:import (java.io File))
  (:gen-class))

(defn secret? [line] 
  (if 
      (or
       (if (re-find #"license_key" line) true false)
       (if (re-find #"secret" line) true false)
       (if (re-find #"VERIFY_NONE" line) true false)
       (if (re-find #"secret_key" line) true false)
       (if (re-find #"secret_token" line) true false)
       (if ; Tweaking this to find the cases we want...
        (and 
          (if (re-find #"password=" line) true false)
          ;(if (re-find #"=" line) true false)
          ;(if (re-find #"\"password\"" line) false true)
         ) true false
        )
       (if (re-find #"passwd" line) true false)
       ; These below were too false positive prone.
       ;(if (re-find #"pw" line) true false)
       ;(if (re-find #"private" line) true false)
       ;(if (re-find #"key" line) true false)
       ) 
    (do
      (report "Testing ... " "Potential secret in file." (.toString line))
;      (println line) 
      true)
    false)
  )

(defn secret-not-excluded? [file]
  (or 
   (.endsWith (.toString file) ".yml")
   (.endsWith (.toString file) ".rb")
   (.endsWith (.toString file) ".java")
   (.endsWith (.toString file) ".coffee")
   ; Handle translation files...
   (.endsWith (.toString file) ".properties")
   (.endsWith (.toString file) ".txt")
   (.endsWith (.toString file) ".erb")
   (.endsWith (.toString file) ".js")
   (.endsWith (.toString file) ".json")
   (.endsWith (.toString file) ".ini") 
   (.endsWith (.toString file) ".sh")   
    ; add more or make this work better.
  )
)

(defn find-secrets [root]
  (debug (str "Looking for project secrets in " (.toString root)))
  (metafo (str "Found "
   (reduce
    +
      (for [file (file-seq (File. root))
          :when (secret-not-excluded? file)]
        (with-open [rdr (clojure.java.io/reader file)]
          (count 
            (filter 
              secret? ;file 
                (line-seq rdr)
            )
          )
        )
      )
    ) 
      " potential secrets."
    )
  )
)

; These are just file extension matchers for specific files that are likely 
; to be sensitive.
(defn file-secret-not-excluded? [file]
  (if   
    (or 
      (.endsWith (.toString file) ".key")
      (.endsWith (.toString file) ".pem")
      (.endsWith (.toString file) ".cert")
      (.endsWith (.toString file) ".crt")
      (.endsWith (.toString file) ".p12")
      (.endsWith (.toString file) ".keystore")
      (.endsWith (.toString file) "id_rsa")
    )
    (do 
      (report (.toString file) "Potential secrete file" "N/A")
      true
    ) 
    false 
  )
)

; Scour the root recursively looking for potentially sensitive files.
(defn find-secret-files [root]
  (debug (str "Looking for files with secrets in " (.toString root)))
  (metafo (str "Found " 
    (reduce 
      +
      (for [file (file-seq (File. root))
        :when (file-secret-not-excluded? file)]
        ; The function file-secret-not-excluded? does the reporting of the specific issues.
        ; This is just for summarization.
        1 ; Because we found one.
      )
    )
    " potentially sensitive files")
  )
)

