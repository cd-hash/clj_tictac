(ns tictac.core
  (:require [tictac.square :as square])
  (:require [tictac.state :as state]))

(defn check-player
  [player-mark]
  (case player-mark
    :x player-mark
    :o player-mark
    nil))

(defn place-mark
  [game-board location player-mark]
  (let [valid-location? (get game-board location)]
    (case valid-location?
      :empty (assoc game-board location player-mark)
      (:x :o) (throw (ex-info
                      "board location occupied"
                      {:player-mark player-mark}))
      nil (throw (ex-info
                  "invalid-location"
                  {:location location})))))

(defn play-move
  [game-board row col player-mark]
  (let [valid-location? (square/create-new-square row col)]
    (if-some [validated-mark (check-player player-mark)]
      (place-mark game-board valid-location? validated-mark)
      (throw (ex-info "invalid-player" {:invalid-player player-mark})))))

(defn get-col-vals
  [board col]
  (for [position (keys board)
        :when (= (get position :col) col)]
    (get board position)))

(defn get-row-vals
  [board row]
  (for [position (keys board)
        :when (= (get position :row) row)]
    (get board position)))

(defn get-diag-vals
  [board]
  (concat
   [(for [position (keys board)
          :when (= (get position :col) (get position :row))]
      (get board position))]
   [(for [position (keys board)
          :when (= (+ (get position :col) (get position :row)) 4)]
      (get board position))]))

(defn won-line?
  [line player-mark]
  (every? #(= player-mark %) line))

(defn check-for-winner
  [board player-mark]
  (if (let [cols (map #(get-col-vals board %) (range 1 4))
            rows (map #(get-row-vals board %) (range 1 4))
            diagonals (get-diag-vals board)]
        (some #(won-line? % player-mark) (concat cols rows diagonals)))
    player-mark
    false))

(comment
  (try (check-player :r)
       (catch java.lang.IllegalArgumentException invalid-player
         (println "caught" invalid-player)))

  (loop [count 3]
    (let [caught-ex (try
                      (check-player :x)
                      (catch java.lang.IllegalArgumentException invalid-player
                        (println "you gave an invalid-player" invalid-player)
                        (neg? count)))]
      (if caught-ex
        caught-ex
        (recur (dec count)))))
  (def board (square/create-board))
  (def board (place-mark board {:row 1 :col 3} :x))
  (def board (place-mark board {:row 2 :col 2} :x))
  (def board (place-mark board {:row 3 :col 1} :x))
  (def board (play-move board 3 3 :p))
  (get-diag-vals board)
  (check-for-winner board :x)
  board)
