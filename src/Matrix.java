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
                array[i][j] = dotProduct(left.getRowMatrix(i).transpose(), right.getColumnMatrix(j));
        return new Matrix(array);
    }

    public Matrix transpose() {
        double[][] array = new double[columnCount()][rowCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = this.array[j][i];
        return new Matrix(array);
    }

    public double determinant() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Determinant!");
        if (rowCount() == 1)
            return array[0][0];
        double result = 0;
        for (int i = 0; i < array[0].length; i++)
            result += array[0][i] * cofactor(0, i);
        return result;
    }

    public Matrix adjoint() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Adjoint!");
        double[][] array = new double[rowCount()][rowCount()];
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < rowCount(); j++)
                array[i][j] = cofactor(i, j);
        return new Matrix(array).transpose();
    }

    public Matrix inverse() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Inverse!");
        double determinant = determinant();
        if (determinant == 0)
            throw new IllegalArgumentException("Singular matrix are not invertible!");
        return scalarProduct(adjoint(), 1 / determinant);
    }

    private double cofactor(int row, int column) {
        return cofactorMatrix(row, column).determinant() * ((row + column) % 2 == 0 ? 1 : -1);
    }

    private Matrix cofactorMatrix(int row, int column) {
        double[][] array = new double[rowCount() - 1][columnCount() - 1];
        for (int i = 0; i < rowCount() - 1; i++)
            for (int j = 0; j < columnCount() - 1; j++)
                array[i][j] = this.array[i < row ? i : i + 1][j < column ? j : j + 1];
        return new Matrix(array);
    }

    // --------------------Checking Operations--------------------------- //

    public boolean isSquareMatrix() {
        return rowCount() == columnCount();
    }

    public boolean isZeroMatrix() {
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < columnCount(); j++)
                if (array[i][j] != 0)
                    return false;
        return true;
    }

    public boolean isSingularMatrix() {
        return determinant() == 0;
    }

    public boolean isUpperTriangleMatrix() {
        if (!isSquareMatrix())
            return false;
        for (int i = 1; i < rowCount(); i++)
            for (int j = 0; j < i; j++)
                if (array[i][j] != 0)
                    return false;
        return true;
    }

    public boolean isLowerTriangleMatrix() {
        if (!isSquareMatrix())
            return false;
        for (int i = 0; i < rowCount(); i++)
            for (int j = i + 1; j < columnCount(); j++)
                if (array[i][j] != 0)
                    return false;
        return true;
    }

    public boolean isDiagonalMatrix() {
        return isUpperTriangleMatrix() && isLowerTriangleMatrix();
    }

    public boolean isIdentityMatrix() {
        if (!isDiagonalMatrix())
            return false;
        for (int i = 0; i < rowCount(); i++)
            if (array[i][i] != 1)
                return false;
        return true;
    }

    public boolean isScalarMatrix() {
        if (!isDiagonalMatrix())
            return false;
        for (int i = 1; i < rowCount(); i++)
            if (array[i][i] != array[0][0])
                return false;
        return true;
    }

    public boolean isSymmetricMatrix() {
        return equals(this, transpose());
    }

    public boolean isSkewSymmetricMatrix() {
        return equals(this, scalarProduct(transpose(), -1));
    }

    public boolean isReducedEchelonForm() {
        double[] leadingRows = new double[rowCount()];
        Arrays.fill(leadingRows, -1);
        for (int i = 0; i < rowCount(); i++) {
            for (int j = 0; j < columnCount(); j++) {
                if (array[i][j] == 1)
                    if (i == 0 || (leadingRows[i - 1] > -1 && leadingRows[i - 1] < j)) {
                        leadingRows[i] = j;
                        break;
                    } else
                        return false;
                else if (array[i][j] != 0)
                    return false;
            }
        }
        return true;
    }

    public boolean isEchelonForm() {
        return isReducedEchelonForm() && !getRowMatrix(rowCount() - 1).isZeroMatrix();
    }

    // --------------------Generation--------------------------- //

    public static Matrix generateIdentityMatrix(int n) {
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

    // --------------------Get Attributes/Values--------------------------- //

    public Matrix getColumnMatrix(int column) {
        if (column < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[][] array = new double[rowCount()][1];
        for (int i = 0; i < rowCount(); i++)
            array[i][0] = this.array[i][column];
        return new Matrix(array);
    }

    public double[] getColumn(int column) {
        if (column < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[] array = new double[rowCount()];
        for (int i = 0; i < rowCount(); i++)
            array[i] = this.array[i][column];
        return array;
    }

    public Matrix getRowMatrix(int row) {
        if (row < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[][] array = new double[1][columnCount()];
        for (int i = 0; i < columnCount(); i++)
            array[0][i] = this.array[row][i];
        return new Matrix(array);
    }

    public double[] getRow(int row) {
        if (row < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        double[] array = new double[columnCount()];
        for (int i = 0; i < columnCount(); i++)
            array[i] = this.array[row][i];
        return array;
    }

    public Matrix subMatrix(int offsetRow, int offsetColumn, int rows, int columns) {
        if (offsetRow < 0 || offsetColumn < 0)
            throw new IllegalArgumentException("Invalid offsets!");
        if (rows < 1 || columns < 1)
            throw new IllegalArgumentException("Invalid rows or columns!");
        if (offsetRow + rows > rowCount() || offsetColumn + columns > columnCount())
            throw new IllegalArgumentException("Out of range!");

        double[][] array = new double[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                array[i][j] = this.array[offsetRow + i][offsetColumn + j];
        return new Matrix(array);
    }

    public double getElement(int row, int column) {
        return array[row][column];
    }

    public void setElement(int row, int column, double value) {
        array[row][column] = value;
    }

    public Matrix deepClone() {
        double[][] array = new double[rowCount()][columnCount()];
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < columnCount(); j++)
                array[i][j] = this.array[i][j];
        return new Matrix(array);
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
        exceptionArithmetic(matrixA, matrixB);
        for (int i = 0; i < matrixA.rowCount(); i++)
            for (int j = 0; j < matrixA.columnCount(); j++)
                if (matrixA.array[i][j] != matrixB.array[i][j])
                    return false;
        return true;
    }

    // --------------------Console Display--------------------------- //

    private static int decimalPrecision = 4;
    private static int decimalPoints = 4;
    private static boolean isFraction = false;
    private static boolean isWhole = false;

    public static void setDisplayAttributes(boolean isFraction, boolean isWhole, int decimalPrecision, int decimalPoints) {
        Matrix.decimalPoints = decimalPoints;
        Matrix.decimalPrecision = decimalPrecision;
        Matrix.isFraction = isFraction;
        Matrix.isWhole = isWhole;
    }

    private String doubleString(double value, int space, boolean negativeSpace, boolean isFraction) {
        var builder = new StringBuilder();
        if (negativeSpace && value >= 0)
            builder.append(" ");
        if (value % 1 == 0)
            builder.append((long) value);
        else if (isFraction) {
            var fraction = MyFunctions.reducedFraction(value, decimalPrecision, isWhole);
            if (fraction.numerator() % fraction.denominator() == 0)
                builder.append(fraction.numerator() * (fraction.isNegative() ? -1 : 1) / fraction.denominator());
            else if (isWhole)
                builder.append(fraction.whole()*(fraction.isNegative() ? -1 : 1)).append(" ").append(fraction.numerator()).append("/").append(fraction.denominator());
            else
                builder.append(fraction.numerator() * (fraction.isNegative() ? -1 : 1)).append("/").append(fraction.denominator());
        } else
            builder.append(value);
        for (int i = builder.length(); i < space; ++i)
            builder.append(" ");
        return builder.substring(0, space);
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
            builder.append("[ ");
            for (double value : data)
                builder.append(doubleString(value, maxDecimal + 2 + Math.min(maxFraction - 2, decimalPoints), negativeSpace, isFraction)).append(" ");
            builder.append("]\n");
        }
        return builder.toString();
    }
}
