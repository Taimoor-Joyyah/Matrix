import java.util.ArrayList;
import java.util.List;

public class MyFunctions {
    private static final List<Long> primeNumbers = new ArrayList<>() {{
        add(2L);
        add(3L);
    }};
    public static int currentPrimeIndex = 0;

    public static long getNextPrimeNumbers() {
        if (currentPrimeIndex < primeNumbers.size())
            return primeNumbers.get(currentPrimeIndex++);
        long value = primeNumbers.get(currentPrimeIndex - 1) + 2;
        while (true) {
            for (int index = 0; value % primeNumbers.get(index) != 0; index++)
                if (primeNumbers.get(index) > Math.sqrt(value)) {
                    primeNumbers.add(value);
                    ++currentPrimeIndex;
                    return value;
                }
            value += 2;
        }
    }

    public static double roundToZero(double value) {
        return 0.000000000000001 > value && value > -0.000000000000001 ? 0 : value;
    }
}
