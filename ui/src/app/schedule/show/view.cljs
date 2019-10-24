(ns app.schedule.show.view
  (:require [re-frame.core           :as rf]
            [app.schedule.crud.form  :as form]
            [app.schedule.show.model :as model]
            [app.pages               :as pages]))

(defn Line
  [people idx]
  [:tr.line {:key idx}
   [:td.sticky-col people]
   [:td.first-col "123"]
   [:td "2"]
   [:td "2"]
   [:td ""]
   [:td "Н"]
   [:td "5"]
   [:td "Н"]
   [:td ""]
   [:td ""]
   [:td "2"]
   [:td "5"]
   [:td "Н"]
   [:td ""]
   [:td "Н"]
   [:td ""]
   [:td "2"]
   [:td "5"]
   [:td ""]
   [:td "5"]
   [:td ""]
   [:td "Н"]
   [:td ""]
   [:td "2"]
   [:td ""]
   [:td ""]
   [:td "5"]
   [:td "Н"]
   [:td "5"]])

(defn Students
  [group]
  [:table.table.table-striped.table-sm.border
   [:thead
    [:tr
     [:th.numeric "№"]
     [:th "Студент"]]]
   [:tbody
    (map-indexed
     (fn [idx people]
       [Line people (inc idx)])
     (:students group))]])

(defn Table
  [group]
  [:div.sticky-table
     [:table.table
      [:thead
       [:tr
        [:th.sticky-col "Студент"]
        [:th.first-col "02.09"]
        [:th "02.09"]
        [:th "03.09"]
        [:th "04.09"]
        [:th "05.09"]
        [:th "06.09"]
        [:th "07.09"]
        [:th "01.09"]
        [:th "02.09"]
        [:th "03.09"]
        [:th "04.09"]
        [:th "05.09"]
        [:th "06.09"]
        [:th "07.09"]
        [:th "01.09"]
        [:th "02.09"]
        [:th "03.09"]
        [:th "04.09"]
        [:th "05.09"]
        [:th "06.09"]
        [:th "07.09"]
        [:th "01.09"]
        [:th "02.09"]
        [:th "03.09"]
        [:th "04.09"]
        [:th "05.09"]
        [:th "06.09"]
        [:th "07.09"]
        [:th "08.09"]]]
      [:tbody
       (map-indexed
        (fn [idx people]
          [Line people (inc idx)])
        (:students group))]]])

(pages/reg-subs-page
 model/index-page
 (fn [{:keys [group shedule]} {id :id}]
   [:div.container.segment.shadow.white
    [:h2 "Журнал"]
    [Table group]]))
