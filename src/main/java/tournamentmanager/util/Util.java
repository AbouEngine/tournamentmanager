package tournamentmanager.util;

public class Util {
    public static boolean isPowerOfTwo(int n) {
        return (n & n - 1) == 0;
    }
}
