(ns game-of-life-cljs.prod
  (:require
    [game-of-life-cljs.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
