import java.util.Arrays;

public class Matrix {
    private final double[][] array;

    public Matrix(double[][] array) {
        if (array == null)
            throw new IllegalArgumentException("Array must not be null!");
        if (array.length == 0 || array[0].length == 0)
            throw new IllegalArgumentException("Rows must not be empty!");
        for (double[] row : array) {
            if (row.length != array[0].length)
                throw new IllegalArgumentException("Every row must have equal columns!");
        }
        this.array = array;
    }

    // --------------------Operations--------------------------- //

    public static Matrix addition(Matrix left, Matrix right) {
        exceptionArithmetic(left, right);
        double[][] array = new double[left.rowCount()][left.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = left.array[i][j] + right.array[i][j];
        return new Matrix(array);
    }

    public static Matrix subtraction(Matrix left, Matrix right) {
        return addition(left, scalarProduct(right, -1));
    }

    public static Matrix scalarProduct(Matrix matrix, double scale) {
        exceptionNull(matrix);
        double[][] array = new double[matrix.rowCount()][matrix.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = matrix.array[i][j] * scale;
        return new Matrix(array);
    }

    public static double dotProduct(Matrix left, Matrix right) {
        exceptionArithmetic(left, right);
        double product = 0;
        for (int i = 0; i < left.rowCount(); i++)
            for (int j = 0; j < left.columnCount(); j++)
                product += left.array[i][j] * right.array[i][j];
        return product;
    }

    public static Matrix matrixProduct(Matrix left, Matrix right) {
        exceptionNull(left);
        exceptionNull(right);
        if (left.columnCount() != right.rowCount())
            throw new IllegalArgumentException("Matrices are invalid for multiplication!");
        double[][] array = new double[left.rowCount()][right.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = dotProduct(transpose(left.getRow(i)),right.getColumn(j));
        return new Matrix(array);
    }

    public static Matrix transpose(Matrix matrix) {
        exceptionNull(matrix);
        double[][] array = new double[matrix.columnCount()][matrix.rowCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = matrix.array[j][i];
        return new Matrix(array);
    }

    // --------------------Checking Operations--------------------------- //

    public boolean isSquareMatrix() {
        return rowCount() == columnCount();
    }

    public boolean isZeroMatrix() {
        for (double[] row : array)
            for (double value : row)
                if (value != 0)
                    return false;
        return true;
    }

    // --------------------Generation--------------------------- //

    public static Matrix generateIdentity(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n must be 1 or greater value!");
        double[][] array = new double[n][n];
        for (int i = 0; i < n; i++)
            array[i][i] = 1;
        return new Matrix(array);
    }

    // --------------------Exceptions--------------------------- //

    private static void exceptionNull(Matrix matrix) {
        if (matrix == null)
            throw new IllegalArgumentException("Matrix must not be null!");
    }

    private static void exceptionUnequalStructure(Matrix matrixA, Matrix matrixB) {
        if (matrixB.rowCount() != matrixA.rowCount() || matrixB.columnCount() != matrixA.columnCount())
            throw new IllegalArgumentException("Rows and columns of both matrices must be equal!");
    }

    private static void exceptionArithmetic(Matrix left, Matrix right) {
        exceptionNull(right);
        exceptionNull(left);
        exceptionUnequalStructure(right, left);
    }

    // --------------------Get Attributes--------------------------- //

    public Matrix getColumn(int column) {
        if (column < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[][] array = new double[rowCount()][1];
        for (int i = 0; i < rowCount(); i++)
            array[i][0] = this.array[i][column];
        return new Matrix(array);
    }

    public Matrix getRow(int row) {
        if (row < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[][] array = new double[1][columnCount()];
        for (int i = 0; i < columnCount(); i++)
            array[0][i] = this.array[row][i];
        return new Matrix(array);
    }

    public double getElement(int row, int column) {
        return array[row][column];
    }

    public double[][] getArrayClone() {
        return array.clone();
    }

    public int columnCount() {
        return array[0].length;
    }

    public int rowCount() {
        return array.length;
    }

    public int size() {
        return rowCount() * columnCount();
    }

    public static boolean equals(Matrix matrixA, Matrix matrixB) {
        return Arrays.deepEquals(matrixA.array, matrixB.array);
    }

    // --------------------Console Display--------------------------- //

    public String doubleString(double value, int space, boolean negativeSpace) {
        var builder = new StringBuilder();
        if (negativeSpace && value >= 0)
            builder.append(" ");
        if (value % 1 == 0)
            builder.append((int) value);
        else
            builder.append(value);
        int size = builder.length();
        for (int i = size; i < space; ++i)
            builder.append(" ");
        return builder.toString().substring(0, space);
    }

    @Override
    public String toString() {
        int maxDecimal = 0;
        int maxFraction = 0;
        boolean negativeSpace = false;
        for (double[] row : array) {
            for (double value : row) {
                if (value < 0)
                    negativeSpace = true;
                maxDecimal = Math.max(maxDecimal, String.valueOf((int) value).length());
                maxFraction = Math.max(maxFraction, String.valueOf(value - (int) value).length());
            }
        }
        var builder = new StringBuilder();
        for (double[] data : array) {
            builder.append("[  ");
            for (double value : data)
                builder.append(doubleString(value, maxDecimal + 1 + Math.min(maxFraction - 2, 2), negativeSpace)).append(" ");
            builder.append("]\n");
        }
        return builder.toString();
    }
}
