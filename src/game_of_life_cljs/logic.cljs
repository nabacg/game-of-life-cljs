(ns game-of-life-cljs.logic)

(defn parse-board [in]
  (->> (mapcat (fn [i y]  (map (fn [j x] [[i j] x]) (range) y)) (range) in)
       (remove (fn [[_ c]] (= c \space)))
       (into {})))

(defn render-board [[grid-size-y grid-size-x] state-map]
  (->> (range grid-size-y)
       (map (fn [y] (->> (range grid-size-x)
                         (map (fn [x] (get state-map [y x] " ")))
                         (clojure.string/join ""))))))


(defn random-board [{:keys [grid-dims] :as game-state}]
  (let [[dim-y dim-x] grid-dims]
    (assoc game-state :board-state
           (->> (range  (* 2 (apply max grid-dims)))
                (mapcat (fn [_]
                          (let [[y x] [(rand-int dim-y) (rand-int dim-x)]]
                            [[[x y] \#]
                             [[(int x) y] \#]
                             [[x (inc y)] \#]])))
                (into {})))))

;(= in (->> (parse-board in) (render-board [6 6])))

(defn neighbour-coords [[grid-size-y grid-size-x] [start-y start-x]]
  (let [range-fn #(range (dec %) (+ % 2))]
    (for [x (range-fn start-x)
          y (range-fn start-y)
          :when (and (>= x 0)
                     (>= y 0)
                     (< x grid-size-x)
                     (< y grid-size-y)
                     (not= [x y] [start-x start-y]))]
      [y x])))

(defn live-neighbours [board-state grid-size coords]
  (->> (neighbour-coords grid-size coords)
       (filter board-state)
       count))

(defn game-rule [cell-alive living-neighbours]
  (cond
    (and cell-alive (< living-neighbours 2)) \space
    (and cell-alive (> living-neighbours 3)) \space
    cell-alive \#
    (and (not cell-alive)
         (= living-neighbours 3)) \#
    :else \space))

(defn game-step [{:keys [grid-dims board-state] :as game-state}]
  (let [[grid-size-y grid-size-x] grid-dims]
    (->> (for [y (range grid-size-y)
               x (range grid-size-x)]
           [[y x] (game-rule
                   (contains? board-state [y x])
                   (live-neighbours board-state grid-dims [y x]))])
         (remove (fn [[_ s]] (= s \space)))
         (into {})
         (assoc game-state :board-state))))
