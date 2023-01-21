(ns tictac.core
  (:require [tictac.square :as square]))

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
  (def board (place-mark board {:row 1 :col 1} :x))
  (def board (place-mark board {:row 2 :col 2} :o))
  board)
