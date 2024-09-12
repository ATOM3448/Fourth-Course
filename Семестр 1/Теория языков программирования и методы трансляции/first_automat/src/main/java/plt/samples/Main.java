package plt.samples;

public class Main {
    public static void main(String[] args) {

        LanguageHandler test = new LanguageHandler("../language.json");

        System.out.println((test.CheckString("-23.65")));
    }
}