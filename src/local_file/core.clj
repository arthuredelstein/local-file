;; Arthur Edelstein 2011
;; Public domain

(ns local-file.core
  (:import (java.io File)))

(defn namespace-to-source [ns]
  "Converts the namespace object to a source (.clj) file path."
  (let [tokens (-> *ns* ns-name str (.split "\\."))]
    (str (apply str (interpose "/" (map munge tokens))) ".clj")))

(defn project-dir
  "Returns the absolute file path of the parent of the src directory
   enclosing the current source file (or namespace's) package dirs."
  ([file]
    (let [rel file]
      (loop [cl (.. Thread currentThread getContextClassLoader)]
        (when cl
          (if-let [url (.findResource cl rel)]
            (-> (.replace (.getFile url) rel "")
                File. .getParentFile .getAbsolutePath)
            (recur (.getParent cl)))))))
  ([]
    (or (project-dir *file*)
        (project-dir (namespace-to-source *ns*))
        (.getAbsolutePath (File. ".")))))

(defn slurp* [f & opts]
  (apply slurp (File. (project-dir) f) opts))

(defn spit* [f content & opts]
  (apply spit (File. (project-dir) f) content opts))