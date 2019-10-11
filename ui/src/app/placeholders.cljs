(ns app.placeholders
  (:require [clojure.string :as str]))

                                        ;Pages
(def location
  {:name             "Аудитории"
   :create           "Добавление аудитории"
   :building         "Корпус"
   :number           "Номер"
   :slots            "Мест"
   :responsible      "Ответсвенный"
   :sign             "Признаки"})

(def groups
  {:name             "Группы"
   :create           "Добавление группы"
   :department       "Кафедра"
   :course           "Курс"
   :students         "Студентов"})

(def faculty
  {:name             "Факультеты"
   :create           "Добавление факультета"
   :faculty-name     "Название"})

                                        ;Inputs
(def search {:hintText          "Поиск..."
             :fullWidth true})
                                        ;Buttons
(def button
  {:create "Создать"
   :save   "Сохранить"
   :delete "Удалить"
   :cancel "Отмена"})


