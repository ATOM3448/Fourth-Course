(defun penultimate_init(s0)
    (defun penultimate(s1)
        (let 
            (
                (a (cdr s1))
            )
            (if (null (cdr a)) (car s1) (penultimate a))
        )
    )
    (cond
        ((null s0) nil)
        ((null (cdr s0)) nil)
        ((not (null (cdr s0))) (penultimate s0))
    )
)

;(print (penultimate_init `(1 2 3 4 5 9 2 5)))
(print (penultimate_init (read)))