(ns tictac.ui
  (:require [clojure.string :as str])
  (:require [clojure.pprint :as pretty]))

(defn get-player
  []
  (println "first player choose x's or o's")
  (keyword (str/trim (read-line))))

(defn trimmed-int
  [raw-str]
  (Integer/parseInt raw-str))

(defn get-location
  []
  (println "where do you want to place your mark?")
  (let [row (trimmed-int (read-line))
        col (trimmed-int (read-line))]
    [row col]))

(comment
  (let [[row col] (get-location)]
    (println row col)))
