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
  [form-path path & [{:keys [placeholder] :as attrs}]]
  (let [node      (rf/subscribe [:zf/node form-path path])
        on-change #(rf/dispatch [:zf/set-value form-path path (.. % -target -value)])]
    (fn [& _]
      (let [{:keys [validators value errors type ] :as ss} @node]
        #_[ui/TextField (merge
                       {:id                (name (first path))
                        :hintText          (get placeholder (first path))
                        :floatingLabelText (get placeholder (first path))
                        :type              (field-type type)
                        :on-change         on-change
                        :errorText         (str/join ", " (vals errors))
                        :value             (or value "")}
                       (dissoc attrs :placeholder))]))))
