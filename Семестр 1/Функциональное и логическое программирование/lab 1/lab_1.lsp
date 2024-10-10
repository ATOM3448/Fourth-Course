; Вариант 7
; Объединить позиции с одинаковым наименованием товаров, количество
; просуммировать, информацию о скидке и цене взять из первой позиции данного
; наименования. Таким образом сформировать новый список чека.

(defun get_ind(s index)
    (cond
        ((null s)
            nil
        )
        ((atom s)
            nil
        )
        ((null index)
            nil
        )
        ((< index 0)
            nil
        )
        ((eql index 0)
            (car s)
        )
        ((eql 1 1)
            (get_ind (cdr s) (1- index))
        )
    )
)

(defun my_union(elem s1)
    (cond
        ((equal (get_ind elem 0) (get_ind (get_ind s1 0) 0))
            (append
                (cdr s1) 
                (list
                    (list 
                        (get_ind elem 0)
                        (get_ind elem 1)
                        (+
                            (get_ind elem 2)
                            (get_ind (get_ind s1 0) 2)
                        )
                        (get_ind elem 3)
                    )
                )
            )
        )
        ((null (cdr s1))
            (append (list elem) s1)
        )
        ((eq 1 1)
            (append (list (get_ind s1 0)) (my_union elem (cdr s1)))
        )
    )
)

(defun grouping(s0)
    (let
        (
            (head (car s0))
            (tail (cdr s0))
        )
        (if (null tail) 
            (list head)
            (my_union head (grouping tail))
        )
    )
)

; Входные данные для теста
; (Товар скидка_% количество цена_за_единицу_товара)
(setq input_values `(
    (Молоко 20 1 200)
    (Мыло 20 2 300)
    (Рис 20 1 76)
    (Сыр 20 1 170)
    (Рис 20 2 76)
    (Молоко 30 1 400)
    (Сало 40 2 300)
    )
)

; Вызов функции и вывод результата
(print (grouping input_values))
;(print (grouping (read)))