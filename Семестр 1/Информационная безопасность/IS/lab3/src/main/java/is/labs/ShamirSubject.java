package is.labs;

public class ShamirSubject {
    private final int a;
    private final int b;
    private final int p;

    public ShamirSubject(int p) {
        a = Util.randomMutuallyPrime(p-1);
        b = Util.euclidAlgorithmInversion(a, p-1);
        this.p = p;
    }

    public int encryptFirstIter(int value) {
        return Util.fastPow(value, a, p);
    }

    public int encryptSecondIter(int value) {
        return Util.fastPow(value, b, p);
    }
}
