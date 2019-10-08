(ns app.placeholders
  (:require [clojure.string :as str]))

                                        ;Pages
(def location
  {:name             "Аудитории"
   :create           "Создание аудитории"
   :building         "Корпус"
   :building-desc    "Назваие корпуса где находиться аудитория"
   :number           "Номер"
   :number-desc      "Номер аудитории"
   :slots            "Мест"
   :slots-desc       "Количество вмещаемых мест"
   :responsible      "Ответсвенный"
   :responsible-desc "за пожарную безопасность"
   :sign             "Признаки"
   :sign-desc        "Признаки, которыми обладает аудитория"})

                                        ;Inputs
(def search {:hintText          "Поиск..."
             :fullWidth true})
                                        ;Buttons
(def button
  {:create "Создать"
   :save   "Сохранить"
   :delete "Удалить"
   :cancel "Отмена"})


