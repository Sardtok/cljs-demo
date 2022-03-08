(ns cljs-demo.handlers
  (:require [re-frame.core :refer [reg-event-db reg-event-fx trim-v]]))

(reg-event-db
  ::init-db
  trim-v
  (fn [db _]
    (js/console.log (clj->js db))
    (merge db {:my-stuff [1 2 3]
               :something-else {:a 1 :b 2 :c 3}})))