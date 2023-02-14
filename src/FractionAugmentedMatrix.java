import java.util.Arrays;

public class FractionAugmentedMatrix {
    private final FractionMatrix left;
    private final FractionMatrix right;

    public FractionAugmentedMatrix(FractionMatrix left, FractionMatrix right) {
        if (left == null || right == null)
            throw new IllegalArgumentException("Matrix should not be null!");
        if (left.rowCount() != right.rowCount())
            throw new IllegalArgumentException("Both matrices should have equal amount of rows!");
        this.left = left.deepClone();
        this.right = right.deepClone();
    }

    public FractionAugmentedMatrix(FractionMatrix left) {
        this(left, FractionMatrix.generateZeroMatrix(left.rowCount(), 1));
    }

    public static FractionMatrix solveX(FractionMatrix matrixA, FractionMatrix matrixB) {
        return FractionMatrix.matrixProduct(matrixA.inverse(), matrixB);
    }

    public FractionAugmentedMatrix reducedEchelonForm() {
        var left = this.left.deepClone();
        var right = this.right.deepClone();
        int[] leadingRows = new int[left.rowCount()];
        int leadingRowCount = 0;
        Arrays.fill(leadingRows, -1);
        for (int col = 0; col < left.columnCount() && leadingRowCount <= left.rowCount(); col++) {
            int index = -1;
            for (int row = 0; row < left.rowCount(); row++) {
                if (!left.getElement(row, col).isZero() && leadingRows[row] == -1) {
                    leadingRows[row] = col;
                    ++leadingRowCount;
                    index = row;
                    break;
                }
            }
            if (index == -1)
                continue;
            reducingRows(left, right, col, index);
        }
        rearrangingRows(left, right, leadingRows);
        return new FractionAugmentedMatrix(left, right);
    }

    private void reducingRows(FractionMatrix left, FractionMatrix right, int column, int index) {
        Fraction tempValue1 = left.getElement(index, column);
        for (int col = 0; col < left.columnCount(); col++)
            left.setElement(index, col, Fraction.division(left.getElement(index, col), tempValue1));
        for (int col = 0; col < right.columnCount(); col++)
            right.setElement(index, col, Fraction.division(right.getElement(index, col), tempValue1));
        for (int row = 0; row < left.rowCount(); row++) {
            Fraction tempValue2 = left.getElement(row, column);
            for (int col = 0; row != index & col < left.columnCount(); col++)
                left.setElement(row, col, Fraction.subtraction(left.getElement(row, col), Fraction.multiplication(left.getElement(index, col), tempValue2)));
            for (int col = 0; row != index & col < right.columnCount(); col++)
                right.setElement(row, col, Fraction.subtraction(right.getElement(row, col), Fraction.multiplication(right.getElement(index, col), tempValue2)));
        }
    }

    private void rearrangingRows(FractionMatrix left, FractionMatrix right, int[] leadingRows) {
        for (int row = 0; row < left.rowCount(); row++) {
            for (int col1 = row; col1 < left.columnCount(); col1++) {
                int index = getIndex(leadingRows, col1);
                if (index != -1 && index >= row) {
                    int temp1 = leadingRows[row];
                    leadingRows[row] = leadingRows[index];
                    leadingRows[index] = temp1;
                    for (int col2 = 0; col2 < left.columnCount(); col2++) {
                        Fraction temp2 = left.getElement(index, col2);
                        left.setElement(index, col2, left.getElement(row, col2));
                        left.setElement(row, col2, temp2);
                    }
                    for (int col2 = 0; col2 < right.columnCount(); col2++) {
                        Fraction temp2 = right.getElement(index, col2);
                        right.setElement(index, col2, right.getElement(row, col2));
                        right.setElement(row, col2, temp2);
                    }
                    break;
                }
            }
        }
    }

    public FractionMatrix getLeft() {
        return left;
    }

    public FractionMatrix getRight() {
        return right;
    }

    private int getIndex(int[] array, int key) {
        for (int i = 0; i < array.length; i++)
            if (array[i] == key)
                return i;
        return -1;
    }

    private int minIndex(double[] array) {
        int min = -1;
        for (int i = 0; i < array.length; i++)
            if (array[i] == 0 || array[i] < array[min])
                min = i;
        return min;
    }

    public boolean isHomogeneousSystem() {
        return right.isZeroMatrix();
    }

    public boolean isConsistentSystem() {
        var system = this;
        if (!left.isReducedEchelonForm())
            system = reducedEchelonForm();
        for (int i = 0; i < system.left.rowCount(); i++)
            if (system.left.getRowMatrix(i).isZeroMatrix() && !system.right.getRowMatrix(i).isZeroMatrix())
                return false;
        return true;
    }

    public int getRank() {
        var system = this;
        if (!left.isReducedEchelonForm())
            system = reducedEchelonForm();
        int nonzeroRows = 0;
        for (int i = 0; i < system.left.rowCount(); i++) {
            if (!system.left.getRowMatrix(i).isZeroMatrix())
                ++nonzeroRows;
        }
        return nonzeroRows;
    }

    public int getNullity() {
        return left.columnCount() - getRank();
    }

    @Override
    public String toString() {
        return left + "\n" + right;
    }
}
