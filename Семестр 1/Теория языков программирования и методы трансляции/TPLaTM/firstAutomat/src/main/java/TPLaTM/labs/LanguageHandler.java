package TPLaTM.labs;

import java.util.HashMap;
import java.util.Map;
import java.io.FileReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Класс определяющий взаимодействие значений с заданным языком
 **/
public class LanguageHandler {

    /**
     * Связка значние - действие
     **/
    private Map<String[], int[]> lang;

    {
        lang = new HashMap<String[], int[]>();
    }

    /**
     * Реализует связку значение - действие по указанным правилам языка
     * @param path Путь к .json файлу с определением языка
     **/
    public LanguageHandler(String path){
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(path)){
            Object obj = parser.parse(reader);

            JSONObject language = (JSONObject) obj;

            // Считываем действия
            int alphabetPower = ((JSONArray) language.get("alphabet")).size();

            int[][] actions = new int[alphabetPower][];

            int actionCount = ((JSONArray) language.get("actions")).size();

            for (int i = 0; i < alphabetPower; i++){
                actions[i] = new int[actionCount];
            }

            actionCount = 0;

            for (Object action:(JSONArray) language.get("actions")){
                int alphCount = 0;
                for (Object act:(JSONArray) action){
                    actions[alphCount++][actionCount] = ((int) (long) act);
                }
                actionCount++;
            }

            // Связываем алфавит с действиями
            int counter = 0;
            for (Object elem:(JSONArray) language.get("alphabet")){
                int[] myAction = new int[actionCount];

                for (int i = 0; i < actionCount; i++){
                    myAction[i] = actions[counter][i];
                }
                counter++;

                lang.put(((String) elem).split("(?<!\\\\)-"), myAction);
            }

        } catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Выводит в поток вывода информацию о языке
     **/
    public void PrintLang()
    {
        for (String[] key:lang.keySet()){
            System.out.println();
            for (String elem:key){
                System.out.print(elem + " ");
            }
            System.out.println("\t");
            for (int elem:lang.get(key)){
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

    /**
     * Возвращает состояния, в которые можно перейти при переданном символе
     * @param value - символ для проверки
     * @return {@code int[]} доступных состояний
     **/
    public int[] GetActions(char value){
        String valueS = String.valueOf(value);
        for (String[] alphabet:lang.keySet()){
            int leftPoint = -1;

            for (String alphaPart:alphabet){
                if (alphaPart.contains(valueS)){
                    return lang.get(alphabet);
                }

                if (leftPoint != -1){
                    if ((leftPoint < value) && (value < alphaPart.charAt(alphaPart.length()-1))){
                        return lang.get(alphabet);
                    }
                }

                if (alphaPart == ""){
                    continue;
                }
                leftPoint = (int) alphaPart.charAt(alphaPart.length()-1);
            }
        }
        return null;
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
                currentQ = GetActions(ch)[currentQ];
            }
        } catch (Exception ex){
            System.err.println(ex.getMessage());
            return false;
        }

        // Ищем в ключах завершение строки и проверяем в дозволительном ли мы для конца состоянии
        for (String[] key:lang.keySet()){
            if (key[0]==""){
                if (lang.get(key)[currentQ] == -1){
                    return true;
                }
            }
        }
        return false;
    }
}