(ns app.placeholders
  (:require [clojure.string :as str]))

(def groups
  {:name             "Группы"
   :create           "Добавление группы"
   :department       "Кафедра"
   :course           "Курс"
   :students_number  "Студентов"})

                                        ;Inputs
(def search {:hintText          "Поиск..."
             :fullWidth true})
                                        ;Buttons
(def button
  {:create "Создать"
   :save   "Сохранить"
   :delete "Удалить"
   :cancel "Отмена"})


