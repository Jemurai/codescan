; Unescaped HTML in code.  
; Premise is that even the presence of 
; tags in code points to paths where content is unescaped coming
; out of the code layer.
(ns jemurai.codescan.checks.unescaped
  (:require 
   [clojure.string :as string]
   )
  (use jemurai.codescan.report)
  (:import (java.io File))
  (:gen-class))

; TO ADD
; find . -name '*.jsp' -type f -exec grep -n3 '${' {} \;


(defn unescaped-html? [line] 
  (if 
      (or
       (if (re-find #"<strong>" line) true false)		; common tags.
       (if (re-find #"<br>" line) true false)
       (if (re-find #"<br/>" line) true false)
       (if (re-find #"<span>" line) true false)
       (if (re-find #"<div>" line) true false)
       (if (re-find #"<form>" line) true false)
       (if (re-find #"<input>" line) true false)
       (if (re-find #"<img>" line) true false)
       (if (re-find #"<script>" line) true false)
       (if (re-find #"<p>" line) true false)
       (if (re-find #"<tr>" line) true false)
       (if (re-find #"<td>" line) true false)
       (if (re-find #"utext" line) true false)			; thymeleaf
       (if (re-find #"html_safe" line) true false)	; rails
       (if (re-find #"raw" line) true false)        ; rails
       (if (re-find #"\{\{\{" line) true false)		  ; mustache
       (if (re-find #"\$\{" line) true false)       ; jsp stl
       ) 
    (do
      (report "File ... " "Potential XSS vector" line) 
      true)
    false)
  )

; Which files do we want to look for web code in?
; Obviously this won't apply to html, erb, etc.
(defn escaped-not-excluded? [file]
  (or 
   (.endsWith (.toString file) ".rb")
   (.endsWith (.toString file) ".java")
   (.endsWith (.toString file) ".js")  ; add more or make this work better.
   )
  )

; Find HTML in source code that might be an indication that there
; is unencoded content being put into the pages.
(defn find-unescaped-html [root]
  (debug "Looking for unescaped html in source code files.")
  (metafo (str "Found "  
   (reduce
    +
    (for [file (file-seq (File. root))
          :when (escaped-not-excluded? file)]
      (with-open [rdr (clojure.java.io/reader file)]
        (println file)
        (count (filter unescaped-html? (line-seq rdr))
        )
      ))) " potential xss to explore." )
    )
  )