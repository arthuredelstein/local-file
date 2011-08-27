To use this very small clojure library, add [local-file "0.0.2"] to the dependencies list in your project.clj file.

To get the current clojure project's directory (the parent dir of the source dir) call project-dir with no arguments. The file* function takes a relative path and returns an absolute path relative to the project directory.

To read and write files in a clojure project's directory, use spit* and slurp* with the same signatures as the standard spit and slurp calls.
  
