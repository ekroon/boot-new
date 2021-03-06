(ns boot.generate.def
  (:require [boot.generate.ns :as gen-ns]
            [boot.new.templates :as tmpl]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn generate
  "Append the specified definition to the specified namespace.
  Create the namespace if necessary. The symbol should be
  fully-qualified: my.ns/my-var"
  [prefix name & [body]]
  (let [[ns-name the-sym] (str/split name #"/")
        path (tmpl/name-to-path ns-name)
        ns-file (io/file (str prefix "/" path ".clj"))]
    (when-not (.exists ns-file)
      (gen-ns/generate prefix ns-name))
    (tmpl/->files {:prefix prefix :path path}
                  ["{{prefix}}/{{path}}.clj"
                   (str "\n(def " the-sym
                        "\n  \"Generated by Boot\""
                        "\n  "body")\n")
                   :append true])))
