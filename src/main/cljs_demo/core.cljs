(ns cljs-demo.core
      (:require
        [cljs-demo.handlers :as handlers]
        [cljs-demo.subs :as subs]
        [reagent.dom :as reagent-dom]
        [re-frame.core :as re-frame]))

(defn root []
  (let [my-stuff (re-frame/subscribe [::subs/my-stuff])]
    [:<>
     [:h1 "Hello World!"]
     [:ul
      (map-indexed (fn [index stuff] [:li {:key index} stuff]) @my-stuff)]]))

(defn ^:dev/after-load init []
      (reagent-dom/render [root]
                          (.getElementById js/document "root")))

(defn ^:export run []
  (init)
  (re-frame/dispatch [::handlers/init-db]))
