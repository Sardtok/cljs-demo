(ns cljs-demo.core
      (:require
        [cljs-demo.handlers :as handlers]
        [cljs-demo.subs :as subs]
        [reagent.dom :as reagent-dom]
        [re-frame.core :as re-frame]))

(defn show-stuff [stuff]
  (cond (sequential? stuff)
        [:ul (map-indexed
               (fn [index stuff] [:li {:key index} stuff])
               stuff)]
        (associative? stuff)
        [:dl (map (fn [[key value]] [:<> {:key key} [:dd key] [:dt value]])
                  stuff)]
        :else [:p [:strong "What is this stuff? " (str stuff)]]))

(defn root []
  (let [my-stuff (re-frame/subscribe [::subs/my-stuff])
        something-else (re-frame/subscribe [::subs/something-else])]
    [:<>
     [:h1 "Hello World!"]
     [show-stuff @my-stuff]
     [show-stuff @something-else]
     [show-stuff "A string"]
     [show-stuff (js/Date.now)]]))

(defn ^:dev/after-load init []
      (reagent-dom/render [root]
                          (.getElementById js/document "root")))

(defn ^:export run []
  (init)
  (re-frame/dispatch [::handlers/init-db]))
