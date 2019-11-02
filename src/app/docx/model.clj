(ns app.docx.model
  (:require [clojure.java.shell :as shell]
            [clojure.java.io :as io]
            [docx-utils.core :as docx]
            [docx-utils.schema :as schema]))

(comment
  (let [output-file-path (docx/transform (.getPath (io/resource "studentreport.docx"))
                                         [(schema/transformation :replace-text-inline "professor"  "Пупкин Анатолий Хренваманеавтоматович")
                                          (schema/transformation :replace-text-inline "student"    "Багров Иван Владимирович")
                                          (schema/transformation :replace-text-inline "group"      "КТ-41-17")
                                          (schema/transformation :replace-text-inline "discipline" "Программирование")
                                          (schema/transformation :replace-text-inline "passes"     "34")
                                          (schema/transformation :replace-text-inline "average"    "2")])]
    (shell/sh "libreoffice" "--norestore" output-file-path)))
