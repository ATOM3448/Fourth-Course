; Вариант 13
; A/x+B=C,
; где A, B, C – неизвестные параметры или выражения с неизвестными параметрами, x
; – искомая переменная. Выразить переменную x через параметры.

; задание уравнения в виде списка
(setq 
    meEquation '(A / x + B = C)
)

; выделение левой части относительно символа op
(defun left(e op)
    (if e
        (let 
            (
                (f (car e))
            )
            (if (eq f op)
                nil
                (cons
                    f 
                    (left (cdr e) op)
                )
            )
        )
        nil
    )
)

; выделение правой части относительно символа op
(defun right(e op)
    (if e
        (let 
            (
                (r (cdr e))
            )
            (if (eq (car e) op)
                r
                (right r op)
            )
        )
        nil
    )
)

; решение уравнения
(defun res(e)
    (let 
        (
            (l (left e '=))
        )
        (if l
            (let 
                (
                    (expr_c (car (right e '=))) 
                    (expr (left l '=))
                )
                (let
                   (
                        (expr_left (left expr '+))
                    )
                    (list
                        (car (right expr_left '/))
                        `=
                        (car (left expr_left '/))
                        `/
                        (list
                            expr_c
                            `-
                            (car (right expr '+))
                        )
                    )
                )
            )
            nil
        )
    )
)

; вывод сообщения об ошибке при неправильно заданном уравнении
(defun res_init(e)
    (let 
        (
            (r (res e))
        )
        (if r
            r
            "Ошибка"
        )
    )
)

; вывод результата решения
(print (res_init meEquation))