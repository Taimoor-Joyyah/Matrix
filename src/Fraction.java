public class Fraction {
    private final long numerator;
    private final long denominator;

    public Fraction(long numerator, long denominator) {
        if (denominator == 0)
            throw new IllegalArgumentException("Denominator must be non-zero value!");
        if (denominator < 0) {
            denominator *= -1;
            numerator *= -1;
        }
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public static Fraction reducedFraction(double value, int precision) {
        int signFactor = value < 0 ? -1 : 1;
        value *= signFactor;
        long precisionPower = (long) Math.pow(10, precision);
        long whole = Math.round(value * precisionPower) / precisionPower;
        long numerator = Math.round(value * precisionPower) % precisionPower;
        long denominator = precisionPower;
        long equivalent = numerator;
        Fraction fraction = new Fraction(numerator, denominator);
        while (numerator != 0) {
            long solution = (long) Math.floor((double) numerator / denominator * precisionPower);
            if (solution > equivalent) {
                --numerator;
            } else if (solution < equivalent) {
                --denominator;
            } else {
                fraction = fraction.reduceRatio();
                numerator = fraction.numerator();
                denominator = fraction.denominator();
                --denominator;
            }
        }
        return new Fraction(signFactor * whole * fraction.denominator() + fraction.numerator(), fraction.denominator());
    }

    private Fraction reduceRatio() {
        if (numerator == 0)
            return new Fraction(0,1);
        long numerator = this.numerator;
        long denominator = this.denominator;
        long sqrtLimit = (long) Math.floor(Math.sqrt(numerator));
        MyFunctions.currentPrimeIndex = 1;
        for (long i = 2; i <= sqrtLimit; i = MyFunctions.getNextPrimeNumbers()) {
            while (numerator % i == 0 && denominator % i == 0) {
                numerator /= i;
                denominator /= i;
            }
        }
        if (denominator % numerator == 0) {
            denominator /= numerator;
            numerator = 1;
        }
        return new Fraction(numerator, denominator);
    }

    private static void exceptionArtithmatic(Fraction left, Fraction right) {
        if (left == null || right == null)
            throw new IllegalArgumentException("Input fractions must not be null!");
    }

    public static Fraction addition(Fraction left, Fraction right) {
        exceptionArtithmatic(left, right);
        return new Fraction(left.numerator * right.denominator + right.numerator * left.denominator, left.denominator * right.denominator).reduceRatio();
    }

    public static Fraction scalarProduct(Fraction fraction, long scale) {
        exceptionArtithmatic(fraction, fraction);
        return new Fraction(scale * fraction.numerator, fraction.denominator).reduceRatio();
    }

    public static Fraction subtraction(Fraction left, Fraction right) {
        return addition(left, scalarProduct(right, -1));
    }

    public static Fraction multiplication(Fraction left, Fraction right) {
        exceptionArtithmatic(left, right);
        return new Fraction(left.numerator * right.numerator, left.denominator * right.denominator).reduceRatio();
    }


    public Fraction inverse() {
        return new Fraction(denominator, numerator);
    }

    public static Fraction division(Fraction left, Fraction right) {
        return multiplication(left, right.inverse());
    }


    public boolean isNegative() {
        return numerator < 0;
    }

    public long whole() {
        return numerator / denominator;
    }

    public long numerator() {
        return numerator;
    }

    public long denominator() {
        return denominator;
    }

    public double value() {
        return (double) numerator / denominator;
    }

    public boolean isZero() {
        return numerator == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Fraction) obj;
        return this.numerator == that.numerator &&
                this.denominator == that.denominator;
    }

    @Override
    public String toString() {
        return numerator * (isNegative() ? -1 : 1) + "/" + denominator;
    }
}
