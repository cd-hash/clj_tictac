(ns tictac.ui
  (:require [clojure.string :as str]))

(defn get-player
  []
  (keyword (str/trim (read-line))))

(comment
  (get-player)
  )
