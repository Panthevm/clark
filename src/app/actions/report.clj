(ns app.actions.report
  (:require [app.db       :refer [db]]
            [clj-pg.honey :as pg]
            [app.actions  :as a]
            [honeysql.core :as hsql]))

(defn student-report
  [& [{:keys [subject schedule]}]]
  {:select   [[(hsql/call :jsonb_build_object
                          (hsql/raw "'subject'")  (hsql/call "->" :a.ass      (hsql/raw "'subject'"))
                          (hsql/raw "'schedule'") (hsql/call "->" :a.resource (hsql/raw "'id'"))
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
              (hsql/call "->" :a.resource (hsql/raw "'id'"))]})
(defn -get
  [{{:keys [subject schedule]} :params}]
  (let [responce (clojure.pprint/pprint (pg/query (db) (student-report {:subject subject :schedule schedule})))]
    (a/ok responce)))
