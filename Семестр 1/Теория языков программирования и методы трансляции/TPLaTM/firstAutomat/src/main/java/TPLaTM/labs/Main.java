package TPLaTM.labs;

public class Main {
    public static void main(String[] args) {

        LanguageHandler test = new LanguageHandler("../language.json");
        test.PrintLang();
        //System.out.println((test.CheckString("-23.65")));
    }
}