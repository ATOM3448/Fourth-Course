package is.labs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> input = new ArrayList<>();
        int[][] key = new int[4][4];

        try (FileInputStream in = new FileInputStream("lab2/src/main/resources/text")) {
            int buf = in.read();
            do {
                input.add(buf);
            } while ((buf = in.read()) != -1);
        } catch (Exception ex) {
            System.err.println("Ошибка чтения файла ввода");
            return;
        }

        try (FileInputStream in = new FileInputStream("lab2/src/main/resources/key")) {
            int ind = 0;
            int buf = in.read();
            do {
                key[ind % 4][ind / 4] = buf;
                ind++;
            } while ((buf = in.read()) != -1);
        } catch (Exception ex) {
            System.err.println("Ошибка чтения файла ключа");
            System.err.println(ex.getMessage());
            return;
        }

        AES aes = new AES();
        boolean append = false;

        var inputIterator = input.iterator();

        while (inputIterator.hasNext()) {
            int[] part = new int[16];
            for (int i = 0; (i < 16) && inputIterator.hasNext(); i++) {
                part[i] = inputIterator.next();
            }

            int[] encrypted = aes.encrypt(part, key);
            int[] decrypted = aes.decrypt(encrypted, key);

            try (FileWriter out = new FileWriter("lab2/src/main/resources/encrypted_hex", append)) {
                for (int i : encrypted) {
                    out.write(Integer.toHexString(i));
                    out.write(" ");
                }
            } catch (Exception ex) {
                System.err.println("Ошибка записи файла зашифрованного файла в hex");
                return;
            }

            try (FileOutputStream out = new FileOutputStream("lab2/src/main/resources/encrypted", append)) {
                for (int i : encrypted) {
                    out.write(i);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка записи зашифрованного файла");
                return;
            }

            try (FileOutputStream out = new FileOutputStream("lab2/src/main/resources/decrypted", append)) {
                for (int i : decrypted) {
                    if (i == 0) {
                        break;
                    }
                    out.write(i);
                }
            } catch (Exception ex) {
                System.err.println("Ошибка записи файла расшифровки");
                return;
            }
            append = true;
        }
    }
}