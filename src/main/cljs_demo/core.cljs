(ns cljs-demo.core
      (:require [reagent.dom :as reagent-dom]))

(defn root []
  [:h1 "Hello World!"])

(defn ^:dev/after-load init []
      (reagent-dom/render [root]
                          (.getElementById js/document "root")))
