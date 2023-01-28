(ns tictac.state
  (:require [tictac.square :as square]))

(def game-state {:status :initial
                 :prev-turn nil
                 :curr-turn nil
                 :board (square/create-board)
                 :ui nil})

(defn switch-turn
  [current-player]
  (case current-player
    :x :o
    :o :x))

(defn current-state->new-state
  [current-state update-event]
  (assoc current-state
         :status (:status update-event)
         :prev-turn (:curr-turn current-state)
         :curr-turn (switch-turn (:curr-turn current-state))
         :board (:board update-event)))

(defn current-state->end-state
  [current-state update-event]
  (println update-event)
  (assoc current-state
         :status (:status update-event)
         :turn nil
         :winner (:winner update-event)))

(comment
  (def new-game game-state)
  (def update-map {:status :playing :turn :x :board 1})
  (def new-state (current-state->new-state new-game update-map))
  new-state)
