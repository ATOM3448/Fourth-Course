;Вариант 3
;Напишите функцию (some р1 p2 х), которая проверяет "Существуют ли элементы списка х,
;для которых выполняется предикат р1 или p2? " (р1, p2 - функциональные имена ).

; Первый предикат
(defun my_p1(x)
    (cond
        ((null x) nil)
        ((atom x) nil)
        ((equal x `(3 4 5)) T)
        (nil)
    )
)

; Второй предикат
(defun my_p2(x)
    (cond
        ((null x) nil)
        ((listp x) nil)
        ((equal x 3) T)
        (nil)
    )
)

; Третий предикат
(defun my_p3(x)
    (cond
        ((null x) nil)
        ((listp x) nil)
        ((equal x 4) T)
        (nil)
    )
)

; Проверка соответствует ли x предикату p1 или p2)
(defun my_some (p1 p2 x)
    (let
        (
            (tail (cdr x))
            (input (list `quote (car x)))
        )
        (cond
            (
                (or
                    (eval (list p1 input))
                    (eval (list p2 input))
                ) 
                T
            )
            ((null tail) nil)
            ((my_some p1 p2 tail))
        )
    )
)

(write (my_some 'my_p2 `my_p3 '(7 4 10 -2)))

(write-line "")

(write (my_some 'my_p2 `my_p3 '(7 (3 3 5) 10 -2)))

(write-line "")

(write (my_some 'my_p1 `my_p3 '(7 (3 4 5) 10 -2)))

(write-line "")

(write (my_some 'my_p1 `my_p2 '(7 4 10 -2)))