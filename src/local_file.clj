;; Arthur Edelstein 2011
;; Public domain

(ns local-file
  (:import (java.io File)))

(defn namespace-to-source
  "Converts the namespace object to a source (.clj) file path."
  [ns]
  (when-let [name (try (-> ns ns-name str) (catch Exception e))]
    (let [tokens (.split name "\\.")]
      (str (apply str (interpose File/separator (map munge tokens))) ".clj"))))

(defn find-uncle-file
  "Finds an ancestor directory of file f containing a file uncle."
  [f uncle]
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

(defn file*
  "Takes a relative path and returns a file rooted
   in the project-dir"
  [rel-path]
  (File. (project-dir) rel-path))

(defn slurp*
  "Read a file f, relative to the project dir."
  [f & opts]
  (apply slurp (file* f) opts))

(defn spit*
  "Write a file f, relative to the project dir."
  [f content & opts]
  (apply spit (file* f) content opts))