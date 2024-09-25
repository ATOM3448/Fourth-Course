(defun dublicator(x n)
    (if (not (= 1 n))
        (append (list x) (dublicator x (1- n)))
        (list x)
    )
)

;(print (dublicator `(2 1 2) 5))
(print (dublicator (read) (read)))