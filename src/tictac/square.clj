(ns tictac.square)

(def upper-bound 4)

(def lower-bound 1)

(defn create-new-square
  [row col]
  (when (and (<= lower-bound row (- upper-bound 1)) (<= lower-bound col (- upper-bound 1)))
    {:row row :col col}))

(defn create-squares
  []
  (for [row (range lower-bound upper-bound)
        col (range lower-bound upper-bound)]
    {:row row :col col}))

(comment
  (create-new-square 1 1)
  (create-squares))
