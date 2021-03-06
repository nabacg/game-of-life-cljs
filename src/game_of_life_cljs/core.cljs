(ns game-of-life-cljs.core
    (:require
     [reagent.core :as r]
     [game-of-life-cljs.logic :as game]))

(def board-dims [30 50])

(defn init-board [board-dims]
  (game/random-board
   {:grid-dims board-dims
    :board-state nil}))

(def game-state (r/atom (init-board board-dims)))

;; -------------------------
;; Views

(defn board-view [game-state [x-dim y-dim]]
  [:div.ui.grid.padded
   (map (fn [y]
          [:div.row
           ^{:key (str "grid-row-" y)}
           (map
            (fn [x]
              [:div
               ^{:key (str "grid-column-" x)}
               {:class (if (game-state [y x])
                         "column black"
                         "column")}])
            (range x-dim))])
        (range y-dim))])

(defn slider [index value min max]
  [:input {:type "range" :value value :min min :max max
           :style {:width "100%"}
           :on-change (fn [e]
                        (swap! game-state assoc-in 
                        [:grid-dims index] (.. e -target -value))
                        )}])


(defn generation-timer []
  (let [timer-state (r/atom 0)]
    (fn []
      (when  (> @timer-state 0)
        (js/setTimeout
         #(swap! timer-state
                 (fn [v]
                   (if (> v 0)
                     (do
                       (swap! game-state game/game-step)
                       (inc v)) v)))
         1000))
      [:div.menu
       [:button.ui.button {:on-click #(swap! timer-state (fn [i] (if (= i 0) 1 0)))}
        (if (= @timer-state 0) "Start timer" "Stop timer")]
       [:div.right.aligned
        [:label.aligned (str "Iterations:") @timer-state]]])))

(defn home-page []
  [:div.ui.text.container
   [:h1 "Game of Life"]
   [:div.ui.menu
    [:button.ui.button {:on-click #(swap! game-state game/game-step)}
     "Next Generation"]
    [:button.ui.button {:on-click #(reset! game-state (game/random-board @game-state))}
     "Reset grid"
    ]
    [generation-timer]]
      
   [:div.ui.divider]
   [:div.ui.container
    (let [[width height] (:grid-dims @game-state)]
      [:div 
      [:div 
       "Grid width " (str width)
       [slider 0 width 10 100]]
      [:div 
       "Grid heigth " (str height)
       [slider 1 height 10 100]]])
   ] 

   [:div.ui.divider]
   [:div.ui.container
    (let [{:keys [grid-dims board-state]} @game-state]
      [board-view board-state grid-dims])]
   [:div.ui.divider]
   ])

;; -------------------------
;; Initialize app

(defn mount-root []
  (r/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
