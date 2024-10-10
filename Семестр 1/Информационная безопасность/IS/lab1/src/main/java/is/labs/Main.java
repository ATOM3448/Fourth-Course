package is.labs;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HashMap<Character, String[]> language = new HashMap<Character, String[]>();
        int len = 0;
        int power = 0;

        // Считываем таблицу
        try (Scanner reader = new Scanner(Paths.get("lab1/src/main/resources/alphabet.txt"))) {
            len = reader.nextInt();
            power = reader.nextInt();

            for (int i = len; i > 0; i--){
                char symb = Character.toUpperCase(reader.next().charAt(0));
                language.put(symb, new String[power]);
                for (int j = 0; j < power; j++){
                    language.get(symb)[j] = reader.next();
                }
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
            return;
        }

        // Проверяем значения таблицы на пересения
        try{
            Object[] keys = language.keySet().toArray();

            for (int i = len-1; i >= 0; i--){
                for (int j = 0; j < i; j++){
                    List buf = Arrays.asList(language.get(keys[j]));
                    for (int k = power-1; k >= 0; k--){
                        if (buf.contains(language.get(keys[i])[k])){
                            throw new Exception();
                        }
                    }
                }
            }
        } catch (Exception ex){
            System.err.println("Ошибка проверки пересечения множества");
            return;
        }

        // Шифруем
        String encrypted = "";

        try (Scanner reader = new Scanner(Paths.get("lab1/src/main/resources/text.txt"));) {
            Random rnd = new Random();
            try {
                while (true){
                    for (char symb:reader.nextLine().toCharArray()){
                        if (String.valueOf(symb).matches("[ \s]")){
                            encrypted += symb;
                            continue;
                        }
                        encrypted += language.get(Character.toUpperCase(symb))[rnd.nextInt(power)];
                    }
                }
            } catch (NoSuchElementException ex) {
                System.out.println("Файл прочитан");
            }
        } catch (IOException ex){
            System.err.println(ex.getMessage());
            return;
        }

        // Сохраняем зашифрованное
        try (FileWriter writer = new FileWriter("lab1/src/main/resources/encrypted.txt", false))
        {
            writer.write(encrypted);

            writer.flush();
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
        }

        // Дешифруем
        String decrypted = "";
        String code = "";

        for (char symb:encrypted.toCharArray()){
            if (String.valueOf(symb).matches("[ \s]")){
                decrypted += symb;
                continue;
            }
            code += symb;

            for (char key:language.keySet()){
                List buf = Arrays.asList(language.get(key));
                if (buf.contains(code)){
                    code = "";
                    decrypted += key;
                }
            }
        }

        if (code == encrypted){
            System.err.println("Не удалось расшифровать");
            return;
        }

        // Сохраняем расшифрованное
        try (FileWriter writer = new FileWriter("lab1/src/main/resources/decrypted.txt", false))
        {
            writer.write(decrypted);

            writer.flush();
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
        }
    }
}