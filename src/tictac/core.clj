(ns tictac.core
  (:require [tictac.square :as square])
  (:require [tictac.state :as state])
  (:require [tictac.ui :as ui]))

(defn check-player
  [player-mark]
  (case player-mark
    :x player-mark
    :o player-mark))

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

(defn board-full?
  [board]
  (every? #(not= % :empty) (vals board)))

(defn game-play
  [state-map]
  (cond
    (= :game-over (:status state-map)) state-map
    (check-for-winner (:board state-map) (:prev-turn state-map))
    (recur (state/current-state->end-state
            state-map
            {:status :game-over
             :winner (:prev-turn state-map)}))
    (board-full? (:board state-map))
    (recur (state/current-state->end-state
            state-map
            {:status :game-over
             :winner :tie}))
    (= :playing (:status state-map))
    (recur (let [[row col] (ui/get-location)]
             (state/current-state->new-state
              state-map
              {:status :playing
               :board (play-move (:board state-map)
                                 row col
                                 (:curr-turn state-map))})))))

(defn game-start
  [& [_]]
  (let [initial-player (ui/get-player)]
    (-> state/game-state
        (assoc :curr-turn initial-player
               :status :playing
               :prev-turn (state/switch-turn initial-player))
        (game-play))))

(comment
  (game-play (assoc state/game-state
                    :status :game-over))
  (game-start))
