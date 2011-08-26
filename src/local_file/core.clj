;; Arthur Edelstein 2011
;; Public domain

(ns local-file.core
  (:import (java.io File)))

(defn namespace-to-source [ns]
  "Converts the namespace object to a source (.clj) file path."
  (when-let [name (try (-> ns ns-name str) (catch Exception e))]
    (let [tokens (.split name "\\.")]
      (str (apply str (interpose "/" (map munge tokens))) ".clj"))))

(defn find-uncle-file [f uncle]
  "Finds an ancestor directory of file f containing a file uncle."
  (let [f (if (string? f) (File. f) f)
        uncle (if (string? uncle) uncle (.getPath uncle))
        d0 (if (.isDirectory f) f (.getParentFile f))]
    (loop [dir d0]
      (when dir
        (if (.exists (File. dir uncle))
          (.getAbsolutePath dir)
          (recur (.getParentFile dir)))))))

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
        (find-uncle-file (File. ".") "project.clj"))))

(defn slurp* [f & opts]
  (apply slurp (File. (project-dir) f) opts))

(defn spit* [f content & opts]
  (apply spit (File. (project-dir) f) content opts))