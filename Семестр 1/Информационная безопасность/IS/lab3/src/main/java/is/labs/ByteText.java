package is.labs;

import java.util.HashMap;

public class ByteText {
    private static HashMap<Character, Integer> table = new HashMap<>();
    private static HashMap<Integer, Character> invTable = new HashMap<>();

    static {
        table.put('А', 10);
        table.put('Б', 11);
        table.put('В', 12);
        table.put('Г', 13);
        table.put('Д', 14);
        table.put('Е', 15);
        table.put('Ж', 16);
        table.put('З', 17);
        table.put('И', 18);
        table.put('Й', 19);
        table.put('К', 20);
        table.put('Л', 21);
        table.put('М', 22);
        table.put('Н', 23);
        table.put('О', 24);
        table.put('П', 25);
        table.put('Р', 26);
        table.put('С', 27);
        table.put('Т', 28);
        table.put('У', 29);
        table.put('Ф', 30);
        table.put('Х', 31);
        table.put('Ц', 32);
        table.put('Ч', 33);
        table.put('Ш', 34);
        table.put('Щ', 35);
        table.put('Ъ', 36);
        table.put('Ы', 37);
        table.put('Ь', 38);
        table.put('Э', 39);
        table.put('Ю', 40);
        table.put('Я', 41);
        table.put('A', 42);
        table.put('B', 43);
        table.put('C', 44);
        table.put('D', 45);
        table.put('E', 46);
        table.put('F', 47);
        table.put('G', 48);
        table.put('H', 49);
        table.put('I', 50);
        table.put('J', 51);
        table.put('K', 52);
        table.put('L', 53);
        table.put('M', 54);
        table.put('N', 55);
        table.put('O', 56);
        table.put('P', 57);
        table.put('Q', 58);
        table.put('R', 59);
        table.put('S', 60);
        table.put('T', 61);
        table.put('U', 62);
        table.put('V', 63);
        table.put('W', 64);
        table.put('X', 65);
        table.put('Y', 66);
        table.put('Z', 67);
        table.put(' ', 68);
        table.put(',', 69);
        table.put('.', 70);

        invTable.put(10, 'А');
        invTable.put(11, 'Б');
        invTable.put(12, 'В');
        invTable.put(13, 'Г');
        invTable.put(14, 'Д');
        invTable.put(15, 'Е');
        invTable.put(16, 'Ж');
        invTable.put(17, 'З');
        invTable.put(18, 'И');
        invTable.put(19, 'Й');
        invTable.put(20, 'К');
        invTable.put(21, 'Л');
        invTable.put(22, 'М');
        invTable.put(23, 'Н');
        invTable.put(24, 'О');
        invTable.put(25, 'П');
        invTable.put(26, 'Р');
        invTable.put(27, 'С');
        invTable.put(28, 'Т');
        invTable.put(29, 'У');
        invTable.put(30, 'Ф');
        invTable.put(31, 'Х');
        invTable.put(32, 'Ц');
        invTable.put(33, 'Ч');
        invTable.put(34, 'Ш');
        invTable.put(35, 'Щ');
        invTable.put(36, 'Ъ');
        invTable.put(37, 'Ы');
        invTable.put(38, 'Ь');
        invTable.put(39, 'Э');
        invTable.put(40, 'Ю');
        invTable.put(41, 'Я');
        invTable.put(42, 'A');
        invTable.put(43, 'B');
        invTable.put(44, 'C');
        invTable.put(45, 'D');
        invTable.put(46, 'E');
        invTable.put(47, 'F');
        invTable.put(48, 'G');
        invTable.put(49, 'H');
        invTable.put(50, 'I');
        invTable.put(51, 'J');
        invTable.put(52, 'K');
        invTable.put(53, 'L');
        invTable.put(54, 'M');
        invTable.put(55, 'N');
        invTable.put(56, 'O');
        invTable.put(57, 'P');
        invTable.put(58, 'Q');
        invTable.put(59, 'R');
        invTable.put(60, 'S');
        invTable.put(61, 'T');
        invTable.put(62, 'U');
        invTable.put(63, 'V');
        invTable.put(64, 'W');
        invTable.put(65, 'X');
        invTable.put(66, 'Y');
        invTable.put(67, 'Z');
        invTable.put(68, ' ');
        invTable.put(69, ',');
        invTable.put(70, '.');
    }

    private ByteText() {
    }
    
    public static int[] getBytes(String text) {
        char[] chars = text.toUpperCase().toCharArray();
        int[] out = new int[chars.length];
        
        for (int i = 0; i < chars.length; i++) {
            out[i] = table.get(chars[i]);
        }

        return out;
    }

    public static String getString(int[] byteText) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int value : byteText) {
            stringBuilder.append(invTable.get(value));
        }

        return stringBuilder.toString();
    }
}
