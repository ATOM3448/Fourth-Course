package is.labs;

public class AES {
    /**
     * Таблица замен
     */
    private static final int[][] s_box = {
            {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
            {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
            {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
            {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
            {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
            {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
            {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
            {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
            {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
            {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
            {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
            {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
            {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
            {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
            {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
            {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
    };

    /**
     * Инвертированная таблица замен
     */
    private static final int[][] inv_s_box = {
            {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
            {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
            {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
            {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
            {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
            {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
            {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
            {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
            {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
            {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
            {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
            {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
            {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
            {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
            {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
            {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}
    };

    /**
     * Таблица для XOR при  keyExpansion
     */
    private static final int[][] rcon = {
            {0x00, 0x00, 0x00, 0x00},
            {0x01, 0x00, 0x00, 0x00},
            {0x02, 0x00, 0x00, 0x00},
            {0x04, 0x00, 0x00, 0x00},
            {0x08, 0x00, 0x00, 0x00},
            {0x10, 0x00, 0x00, 0x00},
            {0x20, 0x00, 0x00, 0x00},
            {0x40, 0x00, 0x00, 0x00},
            {0x80, 0x00, 0x00, 0x00},
            {0x1b, 0x00, 0x00, 0x00},
            {0x36, 0x00, 0x00, 0x00}
    };

    /**
     * Матрица для {@code mixColumns}
     */
    private static final int[][] MDS = {
            {0x02, 0x03, 0x01, 0x01},
            {0x01, 0x02, 0x03, 0x01},
            {0x01, 0x01, 0x02, 0x03},
            {0x03, 0x01, 0x01, 0x02}
    };

    /**
     * Матрица для {@code invMixColumns}
     */
    private static final int[][] invMDS = {
            {0x0E, 0x0B, 0x0D, 0x09},
            {0x09, 0x0E, 0x0B, 0x0D},
            {0x0D, 0x09, 0x0E, 0x0B},
            {0x0B, 0x0D, 0x09, 0x0E}
    };

    /**
     * Копирует одномерный массив {@code value} в двумерный {@code state}
     *
     * @param value Что копируется
     * @return Двумерный массив
     */
    private int[][] stateMaker(int[] value) {
        int[][] state = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = value[i + 4 * j];
            }
        }

        return state;
    }

    /**
     * XOR {@code state} с {@code w}
     *
     * @param state Значение
     * @param w     Ключ
     * @return {@code state} XOR {@code w}
     */
    private int[][] addRoundKey(int[][] state, int[][] w, int round) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                state[i][j] ^= w[i][(round * 4) + j];
        }

        return state;
    }

    /**
     * Заменяет значения матрицы на значения из таблицы s_box
     *
     * @param value Какие значения заменяются
     * @return Переставленные значения
     */
    private int[][] subBytes(int[][] value) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                value[i][j] = s_box[value[i][j] >>> 4][value[i][j] % 16];
            }
        }

        return value;
    }

    /**
     * Смещает строки в матрице
     *
     * @param value Массив в котором смещаются строки
     * @return Массив со смещенными строками
     */
    private int[][] shiftRows(int[][] value) {
        int[][] result = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = value[i][(i + j) % 4];
            }
        }

        return result;
    }

    /**
     * Умножает {@code MDS} на {@code values}.
     * <p>
     * Вместо сложения XOR.
     * <p>
     * Вместо умножения - разбиение бинарных чисел на полиномы и перемножение полиномов... в теории.
     * По сути, на примере умножения на 03: {@code (число << 1) XOR (число << 0)} упрощаем
     * {@code (число << 1) XOR число}
     *
     * @param values Матрица на которую умножаем
     * @return Результат перемножения
     */
    private int[][] mixColumns(int[][] values) {
        int[][] result = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j][i] = 0;
                for (int k = 0; k < 4; k++) {
                    if ((MDS[j][k] % 2) != 0) {
                        result[j][i] ^= values[k][i];
                    }
                    if (MDS[j][k] > 1) {
                        result[j][i] ^= (values[k][i] << 1);
                    }
                }
                if (result[j][i] > 255) {
                    result[j][i] ^= 283;//res ^ 100011011
                }
            }
        }

        return result;
    }

    /**
     * Выдает набор ключей для раундов шифрования
     *
     * @param key Исходный ключ
     * @return Ключи раунда
     */
    private int[][] keyExpansion(int[][] key) {
        int[][] w = new int[4][4 * 11];

        for (int i = 0; i < 4; i++) {
            System.arraycopy(key[i], 0, w[i], 0, 4);
        }

        for (int j = 4; j < (4 * 11); j++) {
            if (j % 4 == 0) {
                int buf = w[0][j - 1];
                for (int i = 3; i >= 0; i--) {
                    int buf1 = w[i][j - 1];
                    w[i][j] = buf;
                    buf = buf1;
                }

                for (int i = 0; i < 4; i++) {
                    w[i][j] = s_box[w[i][j] >>> 4][w[i][j] % 16];
                }

                for (int i = 0; i < 4; i++) {
                    w[i][j] = w[i][j - 4] ^ w[i][j] ^ rcon[j / 4][i];
                }
                continue;
            }
            for (int i = 0; i < 4; i++) {
                w[i][j] = w[i][j - 1] ^ w[i][j - 4];
            }
        }

        return w;
    }

    /**
     * Копирует двумерный массив {@code state} в одномерный {@code output}
     *
     * @param state Что копируется
     * @return Одномерный массив
     */
    private int[] outputMaker(int[][] state) {
        int[] output = new int[16];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                output[i + 4 * j] = state[i][j];
            }
        }

        return output;
    }

    /**
     * Шифрует переданный {@code input} с использованием {@code key}
     *
     * @param input Значение для шифрования
     * @param key   Ключ шифрования
     * @return Зашифрованное значение
     */
    public int[] encrypt(int[] input, int[][] key) {
        int[][] state = stateMaker(input);
        int[][] w = keyExpansion(key);
        state = addRoundKey(state, w, 0);

        for (int round = 1; round < 10; round++) {
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, w, round);
        }

        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, w, 10);

        return outputMaker(state);
    }

    /**
     * Обратное к {@code shiftRows}
     *
     * @param value Массив в котором смещаются строки
     * @return Массив со смещенными строками
     */
    private int[][] invShiftRows(int[][] value) {
        int[][] result = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = value[i][(j + (4 - i)) % 4];
            }
        }

        return result;
    }

    /**
     * Обработное к {@code subBytes}
     *
     * @param value Какие значения заменяются
     * @return Переставленные значения
     */
    private int[][] invSubBytes(int[][] value) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                value[i][j] = inv_s_box[value[i][j] >>> 4][value[i][j] % 16];
            }
        }

        return value;
    }

    /**
     * Обратная к {@code mixColumns}
     *
     * @param values Матрица на которую умножаем
     * @return Результат перемножения
     */
    private int[][] invMixColumns(int[][] values) {
        int[][] result = new int[4][4];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[j][i] = 0;
                for (int k = 0; k < 4; k++) {
                    int power = -1;
                    for (char value : new StringBuilder(Integer.toBinaryString(invMDS[j][k])).reverse().toString().toCharArray()) {
                        power += 1;
                        if (value != '0') {
                            result[j][i] ^= (values[k][i] << power);
                        }
                    }
                }

                for (int c = 3; c >= 0; c--) {
                    if (result[j][i] >= (256 << c)) {
                        result[j][i] ^= (283 << c);//res ^ 100011011 << c
                    }
                }
            }
        }

        return result;
    }

    /**
     * Расшифрование {@code input}
     *
     * @param input Что расшифровать
     * @param key   Ключ шифрования
     * @return Расшифрованные значения
     */
    public int[] decrypt(int[] input, int[][] key) {
        int[][] state = stateMaker(input);
        int[][] w = keyExpansion(key);

        state = addRoundKey(state, w, 10);

        for (int round = 9; round > 0; round--) {
            state = invShiftRows(state);
            state = invSubBytes(state);
            state = addRoundKey(state, w, round);
            state = invMixColumns(state);
        }

        state = invShiftRows(state);
        state = invSubBytes(state);
        state = addRoundKey(state, w, 0);

        return outputMaker(state);
    }
}
