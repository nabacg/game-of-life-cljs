(ns game-of-life-cljs.logic-test
  (:require [cljs.test :refer-macros [deftest is testing run-tests]]
            [game-of-life-cljs.logic :as l :refer [random-board]]))


(deftest random-board-test
  (let [dims [3 3]
        board (l/random-board {:grid-dims dims})]
    (is (not (nil? (:board-state board))))
    (is (not (nil? (:grid-dims board))))))


(deftest parse-render-board-test
  (let [input ["     "
               "     "
               " ### "
               "     "
               "     "]
        input2 ["      "
                "      "
                "  ### "
                " ###  "
                "      "
                "      "]]
    (testing "Testing parse-board "
      (is (= {[2 1] "#", [2 2] "#", [2 3] "#"}
             (l/parse-board input)))
      (is (= {[2 2] "#", [2 3] "#", [2 4] "#", [3 1] "#", [3 2] "#", [3 3] "#"}
             (l/parse-board input2))))
    (testing "Testing parse -> render is identity"
      (is (= input (l/render-board {:grid-dims [5 5] :board-state  (l/parse-board input)})))
      (is (= input2 (l/render-board {:grid-dims  [6 6] :board-state (l/parse-board input2)}))))))


(deftest neighbour-coords-test
  (is (= [[0 0] [1 0] [2 0] [0 1] [2 1] [0 2] [1 2] [2 2]]
         (l/neighbour-coords [3 3] [1 1])))
  (is (= [[1 1] [2 1] [1 2]]
         (l/neighbour-coords [3 3] [2 2])))
  (is (= [[0 0] [1 0] [1 1] [0 2] [1 2]]
         (l/neighbour-coords [3 3] [0 1])))
  (is (= [[1 0] [2 0] [1 1] [1 2] [2 2]]
         (l/neighbour-coords [3 3] [2 1])))
  (is (= []
         (l/neighbour-coords [3 3] [3 3]))))


(defn game-step-and-render [input]
  (let [dims [(count input) (count (first input))]
        board (l/parse-board input)]
    (->> (l/game-step {:board-state board
                       :grid-dims dims})
         l/render-board)))


(deftest game-step-test
  (is (= ["      "
          "   #  "
          " #  # "
          " #  # "
          "  #   "
          "      "] (game-step-and-render ["      "
                                           "      "
                                           "  ### "
                                           " ###  "
                                           "      "
                                           "      "])))
  (is (= ["      "
          " ##   "
          " #    "
          "    # "
          "   ## "
          "      "] (game-step-and-render ["      "
                                           " ##   "
                                           " ##   "
                                           "   ## "
                                           "   ## "
                                           "      "])))
  (is (= ["     "
          "  #  "
          "  #  "
          "  #  "
          "     "] (game-step-and-render ["     "
                                           "     "
                                           " ### "
                                           "     "
                                           "     "]))))



(enable-console-print!)
(run-tests 'game-of-life-cljs.logic-test)
