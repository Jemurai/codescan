; Injection
;
; Premise is that even the presence of 
; tags in code points to paths where content is unescaped coming
; out of the code layer.
;
(ns jemurai.codescan.checks.injection
 (:require 
	        [clojure.string :as string]
)
  (use jemurai.codescan.report)
  (:import (java.io File))
  (:gen-class))

(defn injectable? [line] 
	(if 
		(or
			(if (re-find #"Runtime.exec" line) true false)
            (if (re-find #"eval" line) true false)
            (if (re-find #"shell_exec" line) true false)
            (if (re-find #"\(read-string" line) true false)
            (if (re-find #"import \"os/exec\"" line) true false)  ; golang

;			(if (re-find #"`" line) true false)
;			(if (re-find #"send" line) true false)
		) 
		(do
			(report "File ... " "Potentially injectable." line) 
			true)
			false)
)

; Obviously this won't apply to html, erb, etc.
(defn injection-not-excluded? [file]
	(or 
		(.endsWith (.toString file) ".rb")
		(.endsWith (.toString file) ".java")
		(.endsWith (.toString file) ".clj")
        (.endsWith (.toString file) ".js")
        (.endsWith (.toString file) ".php")
        (.endsWith (.toString file) ".scala")
        (.endsWith (.toString file) ".go")
	    (.endsWith (.toString file) ".coffee")

        ; add more or make this work better.
	)
)


(defn find-injection [root]
	(metafo "Looking for injection in source code files.")
	(metafo (str "Found "  
		(reduce
			+
		(for [file (file-seq (File. root))
			:when (injection-not-excluded? file)]
			(with-open [rdr (clojure.java.io/reader file)]
			;	(println file)
				(count (filter injectable? (line-seq rdr)))
			)
		)	
		) " potential injection points."
		)
	)
)
