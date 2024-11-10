package is.labs;

public final class Util {
    private Util() {
    }

    public static int fastPow(int value, int power, int mod) {
        int y = 1;
        int s = value % mod;
        for (int i : new StringBuilder(Integer.toBinaryString(power)).reverse().chars().toArray()) {
            if (i == '1') {
                y = (y * s) % mod;
            }
            s = (s * s) % mod;
        }

        return y;
    }

    public static int euclidAlgorithmInversion(int value, int mod) {
        int lastY = 0;
        int currentY = 1;

        int lastValue = mod;

        do {
            int q = lastValue/value;
            int buf = value;
            value = lastValue%value;
            lastValue = buf;

            buf = lastY - currentY*q;
            lastY = currentY;
            currentY = buf;
        } while (value != 0);

        if (lastY < 0) {
            return mod + lastY;
        }

        return lastY;
    }

    public static boolean euclidAlgorithmMutuallyPrime(int value, int mod) {
        int lastValue = mod;

        do {
            int buf = value;
            value = lastValue%value;
            lastValue = buf;
        } while (value != 0);

        return lastValue == 1;
    }

    public static int randomPrime(int max) {
        int result = 0;
        boolean isNotPrime = true;

        global:
        while(isNotPrime) {
            result = (int)(Math.random() * max);

            int half = result/2;

            for (int i = 2; i < half; i++) {
                if (result%i == 0) {
                    continue global;
                }
            }

            isNotPrime = false;
        }

        return result;
    }

    public static int randomMutuallyPrime(int p) {
        int result;
        int pLessOne = p-1;

        do {
            result = randomPrime(pLessOne);
        } while (!euclidAlgorithmMutuallyPrime(result, p));

        return result;
    }
}
