(ns ^:figwheel-no-load game-of-life-cljs.dev
  (:require
    [game-of-life-cljs.core :as core]
    [devtools.core :as devtools]))

(extend-protocol IPrintWithWriter
  js/Symbol
  (-pr-writer [sym writer _]
    (-write writer (str "\"" (.toString sym) "\""))))

(enable-console-print!)

(devtools/install!)

(core/init!)
