(ns cljs-demo.handlers
  (:require [re-frame.core :refer [reg-event-db reg-event-fx trim-v dispatch]]))

(defn fetch [url handler error-handler]
  (-> (.fetch js/self url)
      (.then #(.json %))
      (.then #(js->clj % :keywordize-keys true))
      (.then #(dispatch (if (vector? handler)
                          (conj handler %)
                          [handler %])))
      (.catch #(dispatch (if (vector? error-handler)
                           (conj error-handler %)
                           [error-handler %])))))

(reg-event-fx
  ::fetch-bitcoin-price
  (fn [{db :db} _]
    (if (and (< (:bpi-timer db) (js/Date.now)) (:fetch-bpi db))
      (do (fetch "https://api.coindesk.com/v1/bpi/currentprice.json"
                 ::handle-bitcoin-price
                 ::handle-error)
          {:db (:bpi-timer (+ 5000 (js/Date.now)))
           :fx [[:dispatch-later {:ms 5000
                                  :dispatch [::fetch-bitcoin-price]}]]})
      {:db db})))

(reg-event-db
  ::handle-bitcoin-price
  trim-v
  (fn [db [response]]
    (assoc db :bpi response)))

(reg-event-db
  ::handle-error
  trim-v
  (fn [db [error]]
    (js/console.error error)
    db))

(reg-event-db
  ::init-db
  (fn [db _]
    (js/console.log (clj->js db))
    (merge db {:my-stuff [1 2 3]
               :something-else {:b "The Boldness" :i "Italianness" :s "X STRIKE X"}
               :bpi {}
               :bpi-timer 0
               :fetch-bpi true})))
