(ns cljs-demo.core
  (:require
    [cljs-demo.handlers :as handlers]
    [cljs-demo.subs :as subs]
    [goog.string :as gstring]
    [re-frame.core :as re-frame]
    [reagent.dom :as reagent-dom]))

(defn show-stuff [stuff]
  (cond (sequential? stuff)
        [:ul (map-indexed
               (fn [index stuff] [:li {:key index} stuff])
               stuff)]
        (associative? stuff)
        [:dl (map (fn [[key value]] [:<> {:key key} [:dd key] [:dt value]])
                  stuff)]
        :else [:p [:strong "What is this stuff? " (str stuff)]]))

(defn bpi [bpi]
  [:div
   [:div (get-in bpi [:time :updatedISO])]
   [:div (gstring/unescapeEntities (get-in bpi [:bpi :USD :symbol]))
    (get-in bpi [:bpi :USD :rate])]])

(defn root []
  (let [my-stuff (re-frame/subscribe [::subs/my-stuff])
        something-else (re-frame/subscribe [::subs/something-else])
        bpi-content (re-frame/subscribe [::subs/bpi])]
    [:<>
     [:h1 "BTC Price"]
     ;[show-stuff @my-stuff]
     ;[show-stuff @something-else]
     ;[show-stuff "A string"]
     ;[show-stuff (js/Date.now)]
     [bpi @bpi-content]]))

(defn ^:dev/after-load init []
      (reagent-dom/render [root]
                          (.getElementById js/document "root")))

(defn ^:export run []
  (init)
  (re-frame/dispatch [::handlers/init-db])
  (re-frame/dispatch [::handlers/fetch-bitcoin-price]))
