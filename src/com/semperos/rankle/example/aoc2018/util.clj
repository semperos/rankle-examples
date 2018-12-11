(ns com.semperos.rankle.example.aoc2018.util
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn load-resource
  [resource-name]
  (slurp (io/resource resource-name)))

(defn input-lines
  [resource-name]
  (str/split-lines (load-resource resource-name)))
