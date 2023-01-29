import java.util.ArrayList;
import java.util.List;

public class MyFunctions {
    private static final List<Long> primeNumbers = new ArrayList<>() {{
        add(2L);
        add(3L);
    }};
    private static int currentPrimeIndex = 0;

    public static Fraction reducedFraction(double value, int precision, boolean isWhole) {
        int signFactor = value < 0 ? -1 : 1;
        value *= signFactor;
        long precisionPower = (long) Math.pow(10, precision);
        long roundedValue = Math.round(value * precisionPower) / precisionPower;
        long numerator = Math.round(value * precisionPower) % precisionPower;
        long denominator = precisionPower;
        long equivalent = numerator;
        Fraction fraction = new Fraction(false,0,numerator, denominator);
        while (numerator != 0) {
            long solution = (long) Math.floor((double) numerator / denominator * precisionPower);
            if (solution > equivalent) {
                --numerator;
            } else if (solution < equivalent) {
                --denominator;
            } else {
                fraction = reduceRatio(new Fraction(false,0,numerator, denominator));
                numerator = fraction.numerator();
                denominator = fraction.denominator();
                --denominator;
            }
        }
        if (isWhole)
            return new Fraction(signFactor == -1, roundedValue, fraction.numerator(), fraction.denominator());
        return new Fraction(signFactor == -1, 0, roundedValue * fraction.denominator() + fraction.numerator(), fraction.denominator());
    }

    private static Fraction reduceRatio(Fraction fraction) {
        long numerator = fraction.numerator();
        long denominator = fraction.denominator();
        long sqrtLimit = (long) Math.floor(Math.sqrt(numerator));
        currentPrimeIndex = 1;
        for (long i = 2; i <= sqrtLimit; i = getNextPrimeNumbers()) {
            while (numerator % i == 0 && denominator % i == 0) {
                numerator /= i;
                denominator /= i;
            }
        }
        if (denominator % numerator == 0) {
            denominator /= numerator;
            numerator = 1;
        }
        return new Fraction(false, 0, numerator, denominator);
    }

    private static long getNextPrimeNumbers() {
        if (currentPrimeIndex < primeNumbers.size())
            return primeNumbers.get(currentPrimeIndex++);
        for (long value = primeNumbers.get(currentPrimeIndex - 1) + 2; ; value += 2)
            for (int index = 0; value % primeNumbers.get(index) != 0; index++)
                if (primeNumbers.get(index) > Math.sqrt(value)) {
                    primeNumbers.add(value);
                    ++currentPrimeIndex;
                    return value;
                }
    }
}
