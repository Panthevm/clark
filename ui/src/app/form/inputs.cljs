(ns app.form.inputs
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [zenform.model :as model]
            [zenform.model  :as zf]))

(rf/reg-event-db
 :zf/items
 (fn [db [_ form-path path items]]
   (update-in db form-path
              (fn [form]
                (assoc-in form (conj (model/get-node-path path) :items)
                          items)))))

(rf/reg-event-db
 :zf/dropdown
 (fn [db [_ form-path path open?]]
   (update-in db form-path
              (fn [form]
                (assoc-in form (conj (model/get-node-path path) :dropdown)
                          open?)))))

(defn field-type
  [type]
  (case type
    :integer "number"
    "text"))

(defn input
  [form-path path & [attrs]]
  (let [node      (rf/subscribe [:zf/node form-path path])
        on-change #(rf/dispatch [:zf/set-value form-path path (.. % -target -value)])]
    (fn [& _]
      (let [{:keys [validators value errors type]} @node]
        [:input.form-control {:id                (name (first path))
                              :class             (:class attrs)
                              :type              (field-type type)
                              :on-change         on-change
                              :value             (or value "")}]))))
(defn *combobox
  [form-path path & [{:keys [placeholder]}]]
  (let [node           (rf/subscribe [:zf/node form-path path])
        init-data      (rf/dispatch  [(:on-search @node) {:path path :form-path form-path}])
        on-change      #(rf/dispatch [(:on-search @node) {:q % :path path :form-path form-path}])
        on-click       (fn [value]
                         (rf/dispatch [:zf/set-value form-path path value])
                         (rf/dispatch [:zf/dropdown  form-path path false]))
        close-dropdown (fn []
                         (rf/dispatch [:zf/dropdown form-path path false])
                         (rf/dispatch [:zf/items    form-path path (:default-items @node)]))
        state          (atom {})
        open-dropdown  (fn []
                         (rf/dispatch [:zf/dropdown form-path path true])
                         (js/setTimeout #(.focus (:focus-node @state)) 100))]
    (fn [& _]
      (let [{:keys [items loading display-path validators errors value dropdown default-items]} @node]
        [:div.combobox
         [:div.input-group
          [:div.input-group-append {:on-click open-dropdown}
           [:span.icon
            (if dropdown [:i.far.fa-chevron-up] [:i.far.fa-chevron-down])]]
          [:span.form-control.value {:on-click open-dropdown}
           (if (empty? value)
             [:text.text-muted placeholder]
             (get-in value display-path))]]
         (when validators
           [:div.invalid-feedback.d-block (str/join ", " (vals errors))])
         (when dropdown
           [:div.menu
            [:div.spinnered.mt-1.input-group
             [:input.form-control.search {:ref         #(swap! state assoc :focus-node %)
                                          :on-blur     (fn [] (js/setTimeout #(close-dropdown) 100))
                                          :placeholder "Поиск..."
                                          :on-change   #(on-change (.. % -target -value))}]
             (when loading
               [:div.input-group-appen
                [:span.form-control
                 [:div.spinner-border.spinner-border-sm]]])]
            [:div.shadow
             (if-let [data (or items default-items)]
               (for [{v :value d :display} data]
                 [:li.list-group-item {:key           (:id v)
                                       :on-mouse-down #(on-click v)}
                  d])
               [:li.list-group-item "Ничего не найдено"])]])]))))

(defn combobox
  [form-path path & [attrs]]
  (let [node (rf/subscribe [:zf/node form-path path])]
    (fn [& _]
      (if @node
        [*combobox form-path path attrs]
        [:div
         [:text "Загрузка"]
         [:input.form-control]]))))
