package is.labs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int p = 16453;

        ShamirSubject alice = new ShamirSubject(p);
        ShamirSubject bob = new ShamirSubject(p);

        StringBuilder stringBuilder = new StringBuilder();
        String text;

        try (Scanner scanner = new Scanner(new File("lab3/src/main/resources/text"))) {
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
            }
        } catch (Exception ex) {
            System.err.println("Ошибка чтения файла ввода");
            return;
        }

        text = stringBuilder.toString();
        int[] textBytes = ByteText.getBytes(text);
        int[] encrypt = new int[textBytes.length];

        // Первая итерация
        for (int i = 0; i < textBytes.length; i++) {
            encrypt[i] = alice.encryptFirstIter(textBytes[i]);
        }

        try (FileOutputStream out = new FileOutputStream("lab3/src/main/resources/encrypted1", false)) {
            for (int i : encrypt) {
                out.write(i);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка записи зашифрованного файла");
            return;
        }

        // Вторая итерация
        for (int i = 0; i < textBytes.length; i++) {
            encrypt[i] = bob.encryptFirstIter(encrypt[i]);
        }

        try (FileOutputStream out = new FileOutputStream("lab3/src/main/resources/encrypted2", false)) {
            for (int i : encrypt) {
                out.write(i);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка записи зашифрованного файла");
            return;
        }

        // Третья итерация
        for (int i = 0; i < textBytes.length; i++) {
            encrypt[i] = alice.encryptSecondIter(encrypt[i]);
        }

        try (FileOutputStream out = new FileOutputStream("lab3/src/main/resources/encrypted3", false)) {
            for (int i : encrypt) {
                out.write(i);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка записи зашифрованного файла");
            return;
        }

        // Четвертая итерация
        for (int i = 0; i < textBytes.length; i++) {
            textBytes[i] = bob.encryptSecondIter(encrypt[i]);
        }

        text = ByteText.getString(textBytes);

        try (FileOutputStream out = new FileOutputStream("lab3/src/main/resources/decrypted", false)) {
            for (int i : text.getBytes()) {
                out.write(i);
            }
        } catch (Exception ex) {
            System.err.println("Ошибка записи зашифрованного файла");
        }


    }
}