(ns app.example)




(= 1 1) ;=> 2

(+ 1 2 3 4) ;=> 10

(conj [1 2] 3) ;=> [1 2 3]

(def a {:a 1})

(get a :a) ;=> 1

(= a {:a 1}) ;=> true

(assoc a :b 2) ;=> {:a 1, :b 2}

(get-in {:a {:b 2}} [:a :b]) ;=> 2
