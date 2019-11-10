(ns app.schedule.report.view
  (:require [re-frame.core           :as rf]
            [app.form.inputs         :as i]
            [app.schedule.report.model :as model]
            [app.helpers             :as h]
            [app.pages               :as pages]))

(defn group-page
  []
  [:div
   [:h2 "Группа"]
   ])

(defn student-page
  []
  [:div
   [:h2 "Отчет о студенте"]
   [i/combobox model/student-path [:student] {:placeholder "Студент"}]
   [:i.far.fa-print.text-warning.point {:title "Скачать документ"}]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [group menu current-page]} {id :id}]
   [:div.container
    [:div.row
     [:div.col-sm-3.segment.sidebar
      [:b "Отчет"]
      (map-indexed
       (fn [idx {:keys [class display ico]}] ^{:key idx}
         [:span {:on-click #(rf/dispatch [::model/change-page display])
                 :class (or class (when (and (zero? idx) (not current-page))
                                    " font-weight-bold"))}
          ico display])
       menu)]
     [:div.col-sm.segment.ml-md-3.mt-3.mt-md-0.form
      (case current-page
        "Группа" [group-page]
        [student-page])]]]))
