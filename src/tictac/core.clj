(ns tictac.core
  (:require [tictac.square :as square]))

(defn check-player
  [player]
  (case player
    :x player
    :o player))

(comment
  (try (check-player :r)
       (catch java.lang.IllegalArgumentException invalid-player
         (println "caught" invalid-player))))
