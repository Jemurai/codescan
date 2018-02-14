(ns jemurai.codescan.files
 (:require 
	[clojure.string :as string]
)
  (:import (java.io File))
  (:gen-class))

; Inspect blank lines.
(defn non-blank? [line] (if (re-find #"\S" line) true false))

(defn utext? [line] (if (re-find #"utext" line) true false))
(defn append? [line] (if (re-find #".append" line) true false))
(defn star? [line] (if (re-find #"import*\\*" line) true false))
(defn systemout? [line] (if (re-find #"System.out.print" line) true false))
(defn raw? [line] (if (re-find #"raw" line) true false))

; Is it SVN
(defn non-svn? [file] (not (.contains (.toString file) ".svn" )))

; Is it a java source file
(defn java-source? [file] (.endsWith (.toString file) ".java"))

(defn web-source? [file] (.endsWith (.toString file) ".html"))

; Intentially vague to catch erb, rb etc.
(defn ruby-source? [file] (.endsWith (.toString file) "*.*rb"))

(defn source? [file]
	 (or 
	   (.endsWith (.toString file) ".rb")
   	   (.endsWith (.toString file) ".java")
   	   (.endsWith (.toString file) ".js")
   	   (.endsWith (.toString file) ".coffee")
   	   (.endsWith (.toString file) ".go")
   	   (.endsWith (.toString file) ".php")
   )
)

(defn web? [file]
	 (or 
	   (.endsWith (.toString file) ".erb")
   	   (.endsWith (.toString file) ".html")
   	   (.endsWith (.toString file) ".phtml")
   	   (.endsWith (.toString file) ".css")
   	   (.endsWith (.toString file) ".scss")
   	   (.endsWith (.toString file) ".sass")
   )
)

; Count lines of code.
(defn loc [root]
	(println "Source lines: ")
	(println 
	(reduce
	 +
	 (for [file (file-seq (File. root ))
	 	:when (and (source? file) (non-svn? file))]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter non-blank? (line-seq rdr)))
		)
	)))
	(println "Web lines: ")
	(println 
	(reduce
	 +
	 (for [file (file-seq (File. root ))
	 	:when (and (web? file) (non-svn? file))]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter non-blank? (line-seq rdr)))
		)
	)))
)

(defn filestats [root]
	; Map reduce this to count by extension.
	(println (str "Files: " (count (file-seq (File. root )))))
)


(defn utext-problems? [root]
	(println "Utext Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (java-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter utext? (line-seq rdr)))
		)
	)))
)

(defn utext-problems? [root]
	(println "Web Utext Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (web-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter utext? (line-seq rdr)))
		)
	)))
)

(defn append-problems? [root]
	(println "Append Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (java-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter append? (line-seq rdr)))
		)
	)))
)

(defn raw-problems? [root]
	(println "Raw Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (ruby-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter raw? (line-seq rdr)))
		)
	)))
)

(defn star-problems? [root]
	(println "Star Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (java-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter star? (line-seq rdr)))
		)
	)))
)

(defn systemout-problems? [root]
	(println "System Out Check")
	(println 
	(reduce
		+
	(for [file (file-seq (File. root))
		:when (java-source? file)]
		(with-open [rdr (clojure.java.io/reader file)]
			(count (filter systemout? (line-seq rdr)))
		)
	)))
)

(defn java-problems? [root]
	(println "Java Check")
	(utext-problems? root)
	(append-problems? root)
	(star-problems? root)
	(systemout-problems? root)
)

(defn html-problems? [root]
	(println "HTML Check")
	(utext-problems? root)
)

(defn rails-problems? [root]
	(println "Rails Check")
	(raw-problems? root)
)

(defn findissues [root]
	(java-problems? root)	
	(html-problems? root)	
	(rails-problems? root)	
	; (println (count (file-seq (File. root ))))
	; (println (map #(.getName %) (.listFiles (File. root ))))
)
