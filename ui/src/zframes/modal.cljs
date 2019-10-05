(ns zframes.modal
  (:require [re-frame.core :as rf]))

(rf/reg-event-db
 :modal
 (fn [db [_ modal]]
   (assoc db :modal modal)))

(rf/reg-sub
 :modal
 (fn [db _]
   (:modal db)))

(defn modal []
  (let [modal* (rf/subscribe [:modal])]
    (fn []
      [:div.zmodal
       (when-let [modal @modal*]
         [:div {:style {:position "fixed"
                        :height "100%"
                        :top 0
                        :width "100%"
                        :background-color "rgba(0, 0, 0, 0.5)"
                        :z-index "99"}
                :on-click (when-not (:persistent modal) #(rf/dispatch [:modal nil]))}
          [:div#exampleModalCenter.modal-fade {:style {:transition "opacity .15s linear"}}
           [:div.modal-dialog.modal-dialog-centered {:role "document"
                                                     :style {:justify-content "center"}}
            [:div.modal-content {:style {:width "auto"}}
             [:div.modal-header
              [:h5#exampleModalCenterTitle.modal-title {:style {:margin 0}}
               (:title modal)]
              [:button.close {:type "button"
                              :on-click #(rf/dispatch [:modal nil])}
               [:span "×"]]]
             [:div.modal-body (:body modal)]
             [:div.modal-footer
              (when-let [accept (:accept modal)]
                [:button.btn.btn-primary {:type "button"
                                          :on-click #(do (when-let [accept-fn (:fn accept)] (accept-fn))
                                                         (rf/dispatch [:modal nil]))}
                 (:text accept)])
              (when-let [cancel (:cancel modal)]
                [:button.btn.btn-secondary {:type "button"
                                            :on-click #(do (when-let [cancel-fn (:fn cancel)] (cancel-fn))
                                                           (rf/dispatch [:modal nil]))}
                 (:text cancel)])]]]]])])))
