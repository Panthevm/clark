(ns app.form.inputs
  (:require [re-frame.core  :as rf]
            [reagent-material-ui.core :as ui]
            [clojure.string :as str]
            [zenform.model  :as zf]))

(defn input
  [form-path path & [attrs]]
  (let [node (rf/subscribe [:zf/node form-path path])
        on-change #(rf/dispatch [:zf/set-value form-path path (.. % -target -value)])]
    (fn [& _]
      (let [{:keys [validators value errors]} @node]
        [ui/TextField (merge
                       {:id (name (first path))
                        :on-change on-change
                        :errorText (str/join ", " (vals errors))
                        :value (or value "")}
                       attrs)]))))