(ns tictac.core
  (:require [tictac.square :as square])
  (:require [tictac.state :as state])
  (:require [clojure.string :as str]))

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
    (check-for-winner (:board state-map) (:turn state-map))
    (println "Congratulations " (:turn state-map) "you won")
    (board-full? (:board state-map))
    (println "Nobody won!")
    (= :x (:turn state-map))
    (recur (state/current-state->new-state state-map {:status :playing
                                                      :board (play-move (:board state-map)
                                                                        (+ (rand-int 3) 1)
                                                                        (+ (rand-int 3) 1)
                                                                        (:turn state-map))}))
    (= :o (:turn state-map))
    (recur (state/current-state->new-state state-map {:status :playing
                                                      :board (play-move (:board state-map)
                                                                        (+ (rand-int 3) 1)
                                                                        (+ (rand-int 3) 1)
                                                                        (:turn state-map))}))))

(comment
  (loop [count 3]
    (let [player-mark (keyword (str/trim (read-line)))
          caught-ex (try
                      (println "Choose :x's or :o's ")
                      (check-player player-mark)
                      (catch java.lang.IllegalArgumentException _
                        (println "you gave an invalid-mark: " player-mark)
                        (neg? count)))]
      (if caught-ex
        caught-ex
        (recur (dec count)))))
  (def initial-state (assoc state/game-state :turn :x))
  (game-play initial-state))
