package TPLaTM.labs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;

/**
 * Класс определяющий взаимодействие значений с заданным языком
 **/
public class LanguageHandler {

    /**
     * Связка значние - действие
     **/
    private Map<String, int[]> lang;

    {
        lang = new HashMap<String, int[]>();
    }

    /**
     * Реализует связку значение - действие по указанным правилам языка
     * @param path Путь к .json файлу с определением языка
     **/
    public LanguageHandler(String path){
        try {
            lang = new ObjectMapper().readValue(new File(path), new TypeReference<>(){});
        } catch (Exception ex) {
            System.err.println("Some err");
        }
    }

    /**
     * Выводит в поток вывода информацию о языке
     **/
    public void PrintLang()
    {
        for (String key:lang.keySet()){
            System.out.print(key);
            System.out.println("\t");
            for (int elem:lang.get(key)){
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    /**
     * Принимает решение подходит или нет введенная строка под требования языка
     * @param value - строка для проверки
     * @return {@code true} - если строка прошла проверку; {@code false} - иначе
     **/
    public boolean CheckString(String value){
        int currentQ = 0;

        try{
            for (char ch:value.toCharArray()){
                currentQ = lang.get(String.valueOf(ch))[currentQ];
            }
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            return false;
        }

        return lang.get("\\0")[currentQ] == -1;
    }
}