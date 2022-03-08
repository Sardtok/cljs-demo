(ns cljs-demo.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  ::my-stuff
  (fn [db _] (:my-stuff db)))

(reg-sub
  ::something-else
  (fn [db _] (:something-else db)))
