(ns com.semperos.rankle.example.aoc2018.day01
  "Day 1 of Advent of Code 2018"
  (:refer-clojure :exclude [not + =])
  (:require [clojure.string :as str]
            [com.semperos.rankle
             :as r
             :refer [from in indices not nub-sieve over prefix rot + =]]
            [com.semperos.rankle.example.aoc2018.util :as util]
            [criterium.core :as crit]))

(def read-lines
  "J implementation:

  readlines =: <;._2@:freads"
  (comp (partial mapv #(Long/parseLong %))
        util/input-lines))

(defn solve-a
  "J solution:

  +/ \". every (readlines '/path/to/day01.txt') "
  [nums]
  ((over +) nums))

(defn solve-b-reduce
  [nums]
  (reduce
   (fn [{:keys [set last] :as acc} n]
     (let [freq (+ n last)]
       (if (set freq)
         (reduced freq)
         (-> acc
             (update :set conj freq)
             (assoc :last freq)))))
   {:set #{0} :last 0}
   (cycle nums)))

(defn solve-b-reductions
  [nums]
  (reduce
   (fn [seen n]
     (if (seen n)
       (reduced n)
       (conj seen n)))
   #{}
   (reductions + 0 (cycle nums))))

(defn solve-b-rankle-translation
  "This is a direct translation of the Clojure approach above."
  [nums]
  ((over
    (fn [seen n]
      (if (seen n)
        (reduced n)
        (conj seen n)))
    #{})
   ((prefix (over +)) (cycle nums))))

(defn solve-b-rankle
  "J solution (from https://www.reddit.com/r/adventofcode/comments/a20646/2018_day_1_solutions/eaw4bmg/)

  ({~[:{.@I.-.@~:) +/\\ 1e6 $ nums

  • 1e6 because there isn't a lazy `cycle` in J and we don't reach a repetition
    any earlier.
  • The @ symbol is call \"atop\", used for verb composition.
  • Monadic ~: is the nub sieve (1 for each item when first encountered
    left-to-right, 0 on each subsequent encounter)
  • -. is logical NOT on Iverson booleans, swapping 0 <--> 1
  • Monadic I. returns indices of all 1's in its argument
  • Monadic left-curly-dot takes the head of a sequence
  • The left-square-colon is a verb composition helper.
  • left-curly is `from`
  • The ~ next to left-curly causes the arguments to it to be flipped.

  The J solution does (1) the running sum of 1e6 repetitions of the
  numbers, then in a single composite verb (2) assigns 0's to all the
  duplicate running sums (and 1's to rest), then (3) switches (via
  NOT) that so that duplicates are 1 and non-duplicates are 0,
  then (4) gets the indices of all the 1's within that sequence using
  I., then (5) takes the first of those indices. That
  index-of-the-first-duplicate-running-sum is then (6) passed as the
  first (left) argument to `from`, and the original sequence of
  running sums is the right argument.

  For the input in this repo, that is the running sum at index
  141,669, hence the need for 1e6 repetitions of the original sequence
  to reach that repetition.
  "
  [nums]
  ;; ({~[:{.@I.-.@~:) +/\\ 1e6 $ nums
  (let [all-sums ((prefix (over +)) (take 150000 (cycle nums)))
        uniqs (nub-sieve all-sums)
        dupes (not uniqs)
        dupe-idxs (indices dupes)]
    (from (first dupe-idxs) all-sums)))

(comment
  (require 'com.semperos.rankle :reload-all)

  (solve-a (read-lines "day01.txt"))
  (solve-b-reduce (read-lines "day01.txt"))
  (solve-b-reductions (read-lines "day01.txt"))
  (solve-b-rankle (read-lines "day01.txt"))

  (let [nums (read-lines "day01.txt")]
    (crit/report-result
     (crit/quick-benchmark (solve-b-rankle nums) {})))
  )
