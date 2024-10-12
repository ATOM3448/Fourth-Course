package is.labs;

public class Main {
    public static void main(String[] args) {
        int[] input = {0x69, 0x6E, 0x63, 0x6F, 0x6D, 0x70, 0x72, 0x65, 0x68, 0x65, 0x6E, 0x73, 0x69, 0x62, 0x6C, 0x65};
        int[][] key = {
                {0x00, 0x04, 0x08, 0x0C},
                {0x01, 0x05, 0x09, 0x0D},
                {0x02, 0x06, 0x0A, 0x0E},
                {0x03, 0x07, 0x0B, 0x0F}
        };

        AES aes = new AES();

        int[] encrypted = aes.encrypt(input, key);

        /*for (int i : encrypted){
            System.out.println(Integer.toHexString(i));
        }*/

        int[] decrypted = aes.decrypt(encrypted, key);

        for (int i : decrypted){
            System.out.println(Integer.toHexString(i));
        }
    }
}