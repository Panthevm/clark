(ns app.form.inputs
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [zenform.model :as model]
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
      (let [{:keys [validators value errors type]} @node]
        [:div.form-group
         [:text label]
         [:input.form-control {:id                (name (first path))
                               :type              (field-type type)
                               :on-change         on-change
                               :value             (or value "")}]]))))

(defn *single-combobox
  [form-path path & [{:keys [placeholder]}]]
  (let [node           (rf/subscribe [:zf/node form-path path])
        state          (atom {})
        display-path   (get-in @node [:items 0 :display :path] path)
        loaded-data    (when-let [on-search (:on-search @node)]
                         (rf/dispatch [on-search {:form-path form-path, :path path}]))
        on-change      (fn [q]
                         (when-let [on-search (:on-search @node)]
                           (rf/dispatch [on-search {:q (.. q -target -value) :path path :form-path form-path}])))
        on-click       (fn [{:keys [value]}]
                         (rf/dispatch [:zf/set-value form-path path value])
                         (rf/dispatch [:zf/update-node-schema form-path path {:dropdown false}]))
        close-dropdown (fn []
                         (rf/dispatch [:zf/update-node-schema form-path path {:dropdown false}]))
        open-dropdown  (fn []
                         (rf/dispatch [:zf/update-node-schema form-path path {:dropdown true}])
                         (js/setTimeout
                          #(when-let [focus-node (:focus-node @state)]
                             (.focus focus-node)) 100))]
    (fn [& _]
      (let [{:keys [items loading validators errors value input dropdown]} @node]
        [:div.combobox.form
         [:div.input-group
          [:div.input-group-append {:on-click open-dropdown}
           [:span.icon
            (if dropdown [:i.far.fa-chevron-up] [:i.far.fa-chevron-down])]]
          [:span.form-control.value {:on-click open-dropdown}
           (if (empty? value)
             [:text.text-muted placeholder]
             (get-in value display-path))]]
         (when validators
           [:div.invalid-feedback {:style {:display "block"}} (str/join ", " (vals errors))])

         (when dropdown
           [:div.menu
            [:div.spinnered.mt-1.input-group
             [:input.form-control.search {:ref         #(swap! state assoc :focus-node %)
                                          :on-blur     (fn [] (js/setTimeout #(close-dropdown) 100))
                                          :placeholder "Поиск..."
                                          :on-change   on-change}]
             (when loading
               [:div.input-group-appen
                [:span.form-control
                 [:div.spinner-border.spinner-border-sm]]])]
            [:div.shadow
             (if (empty? items)
               [:li.list-group-item "Ничего не найдено"]
               (for [{v :value, {d :content} :display :as item} items]
                 [:li.list-group-item
                  {:key           (:id v)
                   :on-mouse-down #(on-click item)}
                  d]))]])]))))

(defn single-combobox
  [form-path path & [attrs]]
  (let [node (rf/subscribe [:zf/node form-path path])]
    (fn [& _]
      (when @node
        [*single-combobox form-path path attrs]))))
