(ns app.actions.report
  (:require [app.db       :refer [db]]
            [clj-pg.honey :as pg]
            [app.actions  :as a]
            [honeysql.core :as hsql]

            [ring.util.response :as r]
            [clojure.java.io :as io]
            [docx-utils.core :as docx]
            [docx-utils.schema :as schema]))

(defn student-report
  [& [{:keys [subject schedule]}]]
  {:select   [[(hsql/call :jsonb_build_object
                          (hsql/raw "'subject'")  (hsql/call "->" :a.ass      (hsql/raw "'subject'"))
                          (hsql/raw "'schedule'") (hsql/call "->" :a.resource (hsql/raw "'id'"))
                          (hsql/raw "'group'")    (hsql/call "->" :a.resource (hsql/raw "'group'"))
                          (hsql/raw "'avg'")      (hsql/call :avg (hsql/call :cast
                                                                             (hsql/call "->>" :a.ass
                                                                                        (hsql/raw "'grade'"))
                                                                             :numeric)))
               :resource]]
   :from     [[{:select [:resource
                         [(hsql/call :jsonb_array_elements
                                     (hsql/call "->" :s.sch
                                                (hsql/raw "'assessment'"))) :ass]]
                :from   [[{:select [:resource
                                    [(hsql/call :jsonb_array_elements
                                                (hsql/call "->" :resource
                                                           (hsql/raw "'schedule'"))) :sch]]
                           :from   [:schedule]}
                          :s]]}
               :a]]
   :where    [:and
              [:in (hsql/call "#>" :a.ass      (hsql/raw "'{subject,id}'")) (map #(hsql/raw (str \' % \') ) subject)]
              [:=  (hsql/call "->" :a.resource (hsql/raw "'id'"))           (hsql/raw (str \' schedule \'))]]

   :group-by [(hsql/call "->" :a.ass      (hsql/raw "'subject'"))
              (hsql/call "->" :a.resource (hsql/raw "'group'"))
              (hsql/call "->" :a.resource (hsql/raw "'id'"))]})
(defn -get
  [{{:keys [subject schedule]} :params}]
  (let [subject (into [] subject)
        ss (hsql/format (student-report {:subject [2] #_subject
                                         :schedule 18 #_schedule}))
        responce (pg/query (db)
                           (student-report {:subject [2] #_subject
                                            :schedule 18 #_schedule}))]
    (a/ok
     (if (vector? subject)
       (a/entry responce) responce))))

(defn -docx
  [req]
  (let [resp (-> (docx/transform (.getPath (io/resource "studentreport.docx"))
                                 [(schema/transformation :replace-text-inline "professor"  "Пупкин Анатолий Хренваманеавтоматович")
                                  (schema/transformation :replace-text-inline "student"    "Багров Иван Владимирович")
                                  (schema/transformation :replace-text-inline "group"      "КТ-41-17")
                                  (schema/transformation :replace-text-inline "discipline" "Программирование")
                                  (schema/transformation :replace-text-inline "passes"     "34")
                                  (schema/transformation :replace-text-inline "average"    "2")])
                 r/file-response
                 (r/header "Content-Disposition" "attachment; filename=\"studentreport.docx\"")
                 (r/content-type "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
        ]
    resp
    ))
