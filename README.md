To use this very small clojure library, add [local-file "0.1.0"] to the dependencies list in your project.clj file. Then call (use 'local-file) or "use" local-file in your (ns...) macro call in a clojure source file.

To get the current clojure project's directory (the parent dir of the source dir) call project-dir with no arguments. The file* function takes a relative path and returns an absolute path relative to the project directory.

To read and write files in a clojure project's directory, use spit* and slurp* with the same signatures as the standard spit and slurp calls.
  
