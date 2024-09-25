(defun fullenght(x)
    (let
        (
            (head (car x))
            (tail (cdr x))
        )
        (cond 
            ((and (null head) (null tail)) 0)
            ((null head) (fullenght tail))
            ((atom head) (+ 1 (fullenght tail)))
            ((listp head) (+ (fullenght head) (fullenght tail)))
        )
    )
)

;(print (fullenght `((1 2 3) (4 (5) 6) (7) nil nil nil 8 nil 9)))
(print (fullenght (read)))