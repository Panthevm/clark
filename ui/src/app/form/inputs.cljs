(ns app.form.inputs
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [zenform.model  :as zf]))

(defn field-type
  [type]
  (case type
    :integer "number"
    "text"))

(defn input
  [form-path path & [{:keys [label] :as attrs}]]
  (let [node      (rf/subscribe [:zf/node form-path path])
        on-change #(rf/dispatch [:zf/set-value form-path path (.. % -target -value)])]
    (fn [& _]
      (let [{:keys [validators value errors type ] :as ss} @node]
        [:div.form-group
         [:text label]
         [:input.form-control {:id                (name (first path))
                               :type              (field-type type)
                               :on-change         on-change
                               :value             (or value "")}]]))))
