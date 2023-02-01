(ns tictac.ui
  (:require [clojure.string :as str]))

(defn get-player
  []
  (println "first player choose x's or o's")
  (keyword (str/trim (read-line))))

(defn show
  [board row col]
  (let [item (get board {:row row :col col})]
    (if (= item :empty)
      (str " ")
      (name item))))

(defn display-board
  [board]
  (print (str (show board 1 1) " | " (show board 1 2) " | " (show board 1 3) "\n"
              "---------\n"
              (show board 2 1) " | " (show board 2 2) " | " (show board 2 3) "\n"
              "---------\n"
              (show board 3 1) " | " (show board 3 2) " | " (show board 3 3) "\n")))

(defn trimmed-int
  [raw-str]
  (Integer/parseInt raw-str))

(defn get-location
  [board]
  (display-board board)
  (println "where do you want to place your mark?")
  (let [row (trimmed-int (read-line))
        col (trimmed-int (read-line))]
    [row col]))

(defn print-end-state
  [end-game-map]
  (display-board (:board end-game-map))
  (if (= (:winner end-game-map) :tie)
    (println "Another tie? That's what's wrong with this game!!!!!")
    (println "Player " (name (:winner end-game-map)) "has won the game!!!!!!")))

(comment)
