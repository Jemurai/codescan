; Report
; 
; Provide a way to capture and report items.
;
(ns jemurai.codescan.report)

(defn report [file issue line]
	(println (str "REPORT -> File: " file " Issue: " issue " Line: " line))
)

(defn metafo [message]
	(println (str message))
)

(defn debug [message]
	(println (str message))
)
