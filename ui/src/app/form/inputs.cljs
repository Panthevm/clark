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

(rf/reg-event-db
 :zf/set
 (fn [db [_ form-path path key v]]
   (update-in db form-path
              (fn [form]
                (assoc-in form (conj (model/get-node-path path) key)
                          v)))))

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
(defn *single-combobox
  [form-path path & [{:keys [placeholder]}]]
  (let [node           (rf/subscribe [:zf/node form-path path])
        state          (atom {})
        loaded-data    (fn []
                         (when-let [on-search (:on-search @node)]
                           (rf/dispatch [on-search {:form-path form-path, :path path}])))
        on-change      (fn [q]
                         (let [q (.. q -target -value)])
                         (rf/dispatch [:zf/set form-path path :input q])
                         (when-let [on-search (:on-search @node)]
                           (rf/dispatch [on-search {:q q, :path path, :form-path form-path}])))
        on-click       (fn [{:keys [value]}]
                         (rf/dispatch [:zf/set-value form-path path value])
                         (rf/dispatch [:zf/set form-path path :dropdown false]))
        close-dropdown (fn []
                         (rf/dispatch [:zf/set form-path path :dropdown false])
                         (loaded-data))
        open-dropdown  (fn []
                         (rf/dispatch [:zf/set form-path path :dropdown true])
                         (rf/dispatch [:zf/set form-path path :input ""])
                         (js/setTimeout
                          #(when-let [focus-node (:focus-node @state)]
                             (.focus focus-node)) 100))]
    (loaded-data)
    (fn [& _]
      (let [{:keys [items loading validators errors select-item value input dropdown]} @node]
        [:div.form-group.z-combobox
         [:div.input-group.i-dropdown.rounded
          [:div.input-group-append.i-icon {:on-click open-dropdown}
           [:span.input-group-text.rounded-left.icon-span
            (if dropdown
              [:i.fas.fa-chevron-up]
              [:i.fas.fa-chevron-down])]]
          [:span.form-control.icon-span {:on-click open-dropdown}
           (if (empty? value)
             [:text.text-muted placeholder]
             (get-in select-item [:value :display]
                     (if-let [display (:display value (get-in value [0 :value :display]))] display value)))]]
         (when validators
           [:div.invalid-feedback {:style {:display "block"}} (str/join ", " (vals errors))])
         (when dropdown
           [:div.row
            [:div.i-menu.col-sm
             [:div.shadow
              [:div.spinnered.mt-1.input-group.border-top
               [:input.form-control.rounded-0 {:ref          #(swap! state assoc :focus-node %)
                                               :on-blur      (fn [] (js/setTimeout #(close-dropdown) 200))
                                               :placeholder  "Поиск..."
                                               :on-change    on-change}]
               (when loading
                 [:div.input-group-append
                  [:span.input-group-text.rounded-0.icon-span
                   [:div.spinner-border.spinner-border-sm.text-secondary {:role "status"}]]])]
              [:div.i-item-list
               (when-not (empty? value)
                 [:li.list-group-item.selected-item
                  (:display select-item)])
               (if (empty? items)
                 [:li.list-group-item "Ничего не найдено"]
                 (for [{v :value, d :display :as item} items
                       :let                            [selected? (= value v)]
                       :when                           (not selected?)]
                   [:li.list-group-item
                    {:key           (:id v)
                     :on-mouse-down #(on-click item)}
                    d]))]]]])]))))

(defn single-combobox
  [form-path path & [attrs]]
  (let [node (rf/subscribe [:zf/node form-path path])]
    (fn [& _]
      (when @node
        [*single-combobox form-path path attrs]))))
