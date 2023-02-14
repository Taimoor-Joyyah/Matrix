import java.util.Arrays;

public class FractionMatrix {
    private final Fraction[][] array;

    public FractionMatrix(Fraction[][] array) {
        if (array == null)
            throw new IllegalArgumentException("Array must not be null!");
        if (array.length == 0 || array[0].length == 0)
            throw new IllegalArgumentException("Rows must not be empty!");
        for (Fraction[] row : array) {
            if (row.length != array[0].length)
                throw new IllegalArgumentException("Every row must have equal columns!");
        }
        this.array = array;
    }


    // --------------------Operations--------------------------- //

    public static FractionMatrix addition(FractionMatrix left, FractionMatrix right) {
        exceptionArithmetic(left, right);
        Fraction[][] array = new Fraction[left.rowCount()][left.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = Fraction.addition(left.array[i][j], right.array[i][j]);
        return new FractionMatrix(array);
    }

    public static FractionMatrix subtraction(FractionMatrix left, FractionMatrix right) {
        return addition(left, scalarProduct(right, new Fraction(-1,1)));
    }

    public static FractionMatrix scalarProduct(FractionMatrix matrix, Fraction scale) {
        exceptionNull(matrix);
        Fraction[][] array = new Fraction[matrix.rowCount()][matrix.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = Fraction.multiplication(matrix.array[i][j], scale);
        return new FractionMatrix(array);
    }

    public static Fraction dotProduct(FractionMatrix left, FractionMatrix right) {
        exceptionArithmetic(left, right);
        Fraction product = new Fraction(0,0);
        for (int i = 0; i < left.rowCount(); i++)
            for (int j = 0; j < left.columnCount(); j++)
                product = Fraction.addition(product, Fraction.multiplication(left.array[i][j], right.array[i][j]));
        return product;
    }

    public static FractionMatrix matrixProduct(FractionMatrix left, FractionMatrix right) {
        exceptionNull(left);
        exceptionNull(right);
        if (left.columnCount() != right.rowCount())
            throw new IllegalArgumentException("Matrices are invalid for multiplication!");
        Fraction[][] array = new Fraction[left.rowCount()][right.columnCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = dotProduct(left.getRowMatrix(i).transpose(), right.getColumnMatrix(j));
        return new FractionMatrix(array);
    }

    public FractionMatrix transpose() {
        Fraction[][] array = new Fraction[columnCount()][rowCount()];
        for (int i = 0; i < array.length; i++)
            for (int j = 0; j < array[0].length; j++)
                array[i][j] = this.array[j][i];
        return new FractionMatrix(array);
    }

    public Fraction determinant() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Determinant!");
        if (rowCount() == 1)
            return array[0][0];
        Fraction result = new Fraction(0,1);
        for (int i = 0; i < array[0].length; i++)
            result = Fraction.addition(result, Fraction.multiplication(array[0][i], cofactor(0, i)));
        return result;
    }

    public FractionMatrix adjoint() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Adjoint!");
        Fraction[][] array = new Fraction[rowCount()][rowCount()];
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < rowCount(); j++)
                array[i][j] = cofactor(i, j);
        return new FractionMatrix(array).transpose();
    }

    public FractionMatrix inverse() {
        if (!isSquareMatrix())
            throw new IllegalArgumentException("Square matrix is required for Inverse!");
        Fraction determinant = determinant();
        if (determinant.value() == 0)
            throw new IllegalArgumentException("Singular matrix are not invertible!");
        return scalarProduct(adjoint(), determinant.inverse());
    }

    private Fraction cofactor(int row, int column) {
        return Fraction.scalarProduct(cofactorMatrix(row, column).determinant(), ((row + column) % 2 == 0 ? 1 : -1));
    }

    private FractionMatrix cofactorMatrix(int row, int column) {
        Fraction[][] array = new Fraction[rowCount() - 1][columnCount() - 1];
        for (int i = 0; i < rowCount() - 1; i++)
            for (int j = 0; j < columnCount() - 1; j++)
                array[i][j] = this.array[i < row ? i : i + 1][j < column ? j : j + 1];
        return new FractionMatrix(array);
    }

    public FractionMatrix getReducedRowEchelon() {
        var augmented = new FractionAugmentedMatrix(this);
        augmented.reducedEchelonForm();
        return augmented.getLeft();
    }

    // --------------------Checking Operations--------------------------- //

    public boolean isSquareMatrix() {
        return rowCount() == columnCount();
    }

    public boolean isZeroMatrix() {
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < columnCount(); j++)
                if (!array[i][j].isZero())
                    return false;
        return true;
    }

    public boolean isSingularMatrix() {
        return determinant().isZero();
    }

    public boolean isUpperTriangleMatrix() {
        if (!isSquareMatrix())
            return false;
        for (int i = 1; i < rowCount(); i++)
            for (int j = 0; j < i; j++)
                if (!array[i][j].isZero())
                    return false;
        return true;
    }

    public boolean isLowerTriangleMatrix() {
        if (!isSquareMatrix())
            return false;
        for (int i = 0; i < rowCount(); i++)
            for (int j = i + 1; j < columnCount(); j++)
                if (!array[i][j].isZero())
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
            if (array[i][i].value() != 1)
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
        return equals(this, scalarProduct(transpose(), new Fraction(-1,1)));
    }

    public boolean isReducedEchelonForm() {
        int[] leadingRows = new int[rowCount()];
        Arrays.fill(leadingRows, -1);
        for (int i = 0; i < rowCount(); i++) {
            for (int j = 0; j < columnCount(); j++) {
                if (array[i][j].value() == 1)
                    if (i == 0 || (leadingRows[i - 1] > -1 && leadingRows[i - 1] < j)) {
                        leadingRows[i] = j;
                        break;
                    } else
                        return false;
                else if (!array[i][j].isZero())
                    return false;
            }
        }
        return true;
    }

    public boolean isEchelonForm() {
        return isReducedEchelonForm() && !getRowMatrix(rowCount() - 1).isZeroMatrix();
    }

    // --------------------Generation--------------------------- //

    public static FractionMatrix combineMatrix(FractionMatrix... matrices) {
        for (FractionMatrix matrix : matrices)
            exceptionArithmetic(matrices[0], matrix);
        int rows = matrices[0].rowCount();
        int columns = matrices[0].columnCount();
        Fraction[][] array = new Fraction[rows * columns][matrices.length];

        for (int i = 0; i < matrices.length; i++)
            for (int j = 0; j < rows; j++)
                for (int k = 0; k < columns; k++)
                    array[j * columns + k][i] = matrices[i].array[j][k];
        return new FractionMatrix(array);
    }

    public static FractionMatrix generateIdentityMatrix(int n) {
        if (n < 1)
            throw new IllegalArgumentException("n must be 1 or greater value!");
        Fraction[][] array = new Fraction[n][n];
        for (int i = 0; i < n; i++)
            array[i][i] = new Fraction(1,1);
        return new FractionMatrix(array);
    }

    public static FractionMatrix generateZeroMatrix(int rows, int columns) {
        if (rows < 1 || columns < 1)
            throw new IllegalArgumentException("n must be 1 or greater value!");
        return new FractionMatrix(new Fraction[rows][columns]);
    }

    // --------------------Exceptions--------------------------- //

    private static void exceptionNull(FractionMatrix matrix) {
        if (matrix == null)
            throw new IllegalArgumentException("Matrix must not be null!");
    }

    private static void exceptionUnequalStructure(FractionMatrix matrixA, FractionMatrix matrixB) {
        if (matrixB.rowCount() != matrixA.rowCount() || matrixB.columnCount() != matrixA.columnCount())
            throw new IllegalArgumentException("Rows and columns of both matrices must be equal!");
    }

    private static void exceptionArithmetic(FractionMatrix left, FractionMatrix right) {
        exceptionNull(right);
        exceptionNull(left);
        exceptionUnequalStructure(right, left);
    }

    // --------------------Get Attributes/Values--------------------------- //

    public FractionMatrix getColumnMatrix(int column) {
        if (column < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        Fraction[][] array = new Fraction[rowCount()][1];
        for (int i = 0; i < rowCount(); i++)
            array[i][0] = this.array[i][column];
        return new FractionMatrix(array);
    }

    public Fraction[] getColumn(int column) {
        if (column < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        Fraction[] array = new Fraction[rowCount()];
        for (int i = 0; i < rowCount(); i++)
            array[i] = this.array[i][column];
        return array;
    }

    public FractionMatrix getRowMatrix(int row) {
        if (row < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        Fraction[][] array = new Fraction[1][columnCount()];
        for (int i = 0; i < columnCount(); i++)
            array[0][i] = this.array[row][i];
        return new FractionMatrix(array);
    }

    public Fraction[] getRow(int row) {
        if (row < 0)
            throw new IllegalArgumentException("Column must be 0 or greater!");
        Fraction[] array = new Fraction[columnCount()];
        for (int i = 0; i < columnCount(); i++)
            array[i] = this.array[row][i];
        return array;
    }

    public FractionMatrix subMatrix(int offsetRow, int offsetColumn, int rows, int columns) {
        if (offsetRow < 0 || offsetColumn < 0)
            throw new IllegalArgumentException("Invalid offsets!");
        if (rows < 1 || columns < 1)
            throw new IllegalArgumentException("Invalid rows or columns!");
        if (offsetRow + rows > rowCount() || offsetColumn + columns > columnCount())
            throw new IllegalArgumentException("Out of range!");

        Fraction[][] array = new Fraction[rows][columns];
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                array[i][j] = this.array[offsetRow + i][offsetColumn + j];
        return new FractionMatrix(array);
    }

    public Fraction getElement(int row, int column) {
        return array[row][column];
    }

    public void setElement(int row, int column, Fraction value) {
        array[row][column] = value;
    }

    public FractionMatrix deepClone() {
        Fraction[][] array = new Fraction[rowCount()][columnCount()];
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < columnCount(); j++)
                array[i][j] = this.array[i][j];
        return new FractionMatrix(array);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FractionMatrix matrix = (FractionMatrix) o;
        return Arrays.equals(array, matrix.array);
    }

    public static boolean equals(FractionMatrix matrixA, FractionMatrix matrixB) {
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
        FractionMatrix.decimalPoints = decimalPoints;
        FractionMatrix.decimalPrecision = decimalPrecision;
        FractionMatrix.isFraction = isFraction;
        FractionMatrix.isWhole = isWhole;
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        for (Fraction[] data : array) {
            builder.append("[ ");
            for (Fraction fraction : data)
                builder.append(fraction).append(" ");
            builder.append("]\n");
        }
        return builder.toString();
    }
}
