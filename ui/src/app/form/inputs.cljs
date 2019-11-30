(ns app.form.inputs
  (:require [re-frame.core  :as rf]
            [clojure.string :as str]
            [app.helpers    :as h]
            [zenform.model :as model]
            [zframes.debounce :as debounce]))

(defn ico-dropdown
  [value]
  (if value
    [:i.far.fa-chevron-up]
    [:i.far.fa-chevron-down]))

(defn ico-spinner
  [value]
  (when value
    [:div.spinner]))

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

(defn input
  [form-path path & [attrs]]
  (let [node      (rf/subscribe [:zf/node form-path path])
        on-change #(rf/dispatch [:zf/set-value form-path path (.. % -target -value)])]
    (fn [& _]
      (let [{:keys [value]} @node]
        [:div.input-group
         [:input.form-control {:id                (name (first path))
                               :class             (:class attrs)
                               :key                (name (first path))
                               :on-change         on-change
                               :value             (or value "")}]]))))

(defn *combobox
  [form-path path & [{:keys [placeholder]}]]
  (let [node           (rf/subscribe [:zf/node form-path path])
        _              (rf/dispatch  [(:on-search @node) {:path path :form-path form-path}])
        on-change      #(debounce/debounce [(:on-search @node) {:q % :path path :form-path form-path}])
        on-click       (fn [value]
                         (h/dispatch-n [[:zf/set-value form-path path value]
                                        (when-let [click (:on-click @node)]
                                          [click value])]))
        close-dropdown (fn []
                         (h/dispatch-n [[:zf/dropdown form-path path false]
                                        [:zf/items    form-path path (:default-items @node)]]))
        state          (atom {})
        open-dropdown  (fn []
                         (rf/dispatch [:zf/dropdown form-path path true])
                         (js/setTimeout #(.focus (:focus @state)) 100))]
    (fn [& _]
      (let [{:keys [items loading display-paths value dropdown default-items]} @node
            data (or items default-items)]
        [:div.combobox.mb-4
         [:div.input-group {:on-click open-dropdown}
          [:div.input-group-append
           [:span.icon [ico-dropdown dropdown]]]
          [:span.form-control
           (if (empty? value)
             [:span.text-muted placeholder]
             [:span (str/join " " (mapv
                                   (fn [path] (get-in value path))
                                   display-paths))])]]
         (when dropdown
           [:div.menu
            [:div.input-group
             [:input.form-control.search {:ref         #(swap! state assoc :focus %)
                                          :on-blur     close-dropdown
                                          :placeholder "Поиск..."
                                          :on-change   #(on-change (.. % -target -value))}]
             [ico-spinner loading]]
            [:div.shadow.items
             (if-not (empty? data)
               (map-indexed
                (fn [idx {v :value d :display}] ^{:key idx}
                  [:li.list-group-item {:on-mouse-down #(on-click v)}
                   d])
                data)
               [:li.list-group-item "Ничего не найдено"])]])]))))

(defn combobox
  [form-path path & [attrs]]
  (let [node (rf/subscribe [:zf/node form-path path])]
    (fn [& _]
      (if @node
        [*combobox form-path path attrs]
        [:input.form-control]))))

(defn merge-input
  [form-path path & [meta]]
  (let [value-path (last path)
        path       (vec (drop-last path))
        node       (rf/subscribe [:zf/node form-path path])
        on-change  #(rf/dispatch [:zf/set-value form-path path
                                  (assoc meta value-path (.. % -target -value))])]
    (fn [& _]
      (let [{:keys [value]} @node]
        [:input.form-control.mb-4 {:on-change on-change
                                   :value     (or (:grade value) "")}]))))
