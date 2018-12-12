(ns com.semperos.rankle.example.aoc2018.day02
  (:require [com.semperos.rankle :as r]
            [com.semperos.rankle.example.aoc2018.util :as util]))

(defn input-lines [] (util/input-lines "day02.txt"))

(defn solve-a-rankle
  "J solution (my own):

  strs=:<;._2 freads '/path/to/day02.txt'
  */ +/ ((>./@:(2&=)) , (>./@:(3&=)))   @:   (# every)   @:   (~:@:(/:~) (<;.1) ]) every strs

  Sorts (/:~) a string and then does not-equal (~:) on that to
  highlight running duplicates. Passes that as x to (<;.1) with y of
  the original string which produced a list of boxes of grouped
  letters.

  I then count each box (# every), which gives a list of the number of
  times a letter appears in the original string.

  I then use another fork that checks both the \"has exactly 2\" and
  \"has exactly 3\" cases and combine those (,) into a two-item list,
  per string in the data set.

  This means I have a N-by-2 table with each item being either a 0 or
  a 1 (representing not having or having the \"exactly 2\" or
  \"exactly 3\" groupings checked above.)

  Addition over (+/) does + between each row (thus summing the
  columns), resulting in a two-item list of \"the number of strings
  with at least one exactly-2\" and \"the numer of strings with at
  least one exactly-3\".

  Multiplication over (*/) puts * between those two sums, giving the
  simple checksum required by the puzzle.
  "
  [lines]
  )

(defn solve-b-rankle
  "J solution (my own)

  N.B. A definition of Levenshtein distance, taken from https://code.jsoftware.com/wiki/Essays/Levenshtein_Distance#Version_Producing_Only_The_Scalar_Distance:
  levdist=: 4 : 0 \" 1
    'a b'=. (/: #&>)x;y
    z=. >: iz =. i.#b
    for_j. a do.
      z=. <./\\&.(-&iz) (>: <. (j ~: b) + |.!.j_index) z
    end.
    {:z
   )

  N.B. My solution
  ss=:> every strs
  idxs=.,(I. (1 = ss levdist/ ss))
  ids=.((idxs > 0) # idxs) { ss
  (=/ ids) # {.ids

  First I unbox the strings up front.

  Then I calculate a table of the Levenshtein distance between every
  string in the data set. I then get the indices in that table where
  the Levenshtein distance was only 1.

  With those table indices in hand, I then get the two indices for the
  string ids that are only a Levenshtein distance of one apart in the
  original sequence of string ids. I compare these with = and then use
  copy # to keep only the common characters, which gives the solution.

  In part (a), I first solved the problem this way (with temporary
  bindings) and then spruced it up by making it mostly tacit. While
  interesting, this is more readable.
  "
  [])

(comment
  (require 'com.semperos.rankle :reload)
  )
