import java.util.Arrays;

public class AugmentedMatrix {

    private static class Solution {
        boolean hasUnique = false;
        final Matrix[] matrices;

        Solution(boolean hasUnique, Matrix... matrices) {
            this.hasUnique = hasUnique;
            this.matrices = matrices;
        }
    }

    private final Matrix left;
    private final Matrix right;

    private final int[] leadingRows;
    private int leadingRowCount = 0;

    public AugmentedMatrix(Matrix left, Matrix right) {
        if (left == null || right == null)
            throw new IllegalArgumentException("Matrix should not be null!");
        if (left.rowCount() != right.rowCount())
            throw new IllegalArgumentException("Both matrices should have equal amount of rows!");
        this.left = left.deepClone();
        this.right = right.deepClone();
        leadingRows = new int[left.rowCount()];
    }

    private AugmentedMatrix(Matrix left, Matrix right, int[] leadingRows, int leadingRowCount) {
        this.left = left.deepClone();
        this.right = right.deepClone();
        this.leadingRows = leadingRows;
        this.leadingRowCount = leadingRowCount;
    }

    public AugmentedMatrix(Matrix left) {
        this(left, Matrix.generateZeroMatrix(left.rowCount(), 1));
    }

    public static Matrix solveX(Matrix matrixA, Matrix matrixB) {
        return Matrix.matrixProduct(matrixA.inverse(), matrixB);
    }

    public AugmentedMatrix reducedEchelonForm() {
        var left = this.left.deepClone();
        var right = this.right.deepClone();
        Arrays.fill(leadingRows, -1);
        for (int col = 0; col < left.columnCount() && leadingRowCount <= left.rowCount(); col++) {
            int index = -1;
            for (int row = 0; row < left.rowCount(); row++) {
                if (left.getElement(row, col) != 0 && leadingRows[row] == -1) {
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
        return new AugmentedMatrix(left, right, leadingRows, leadingRowCount);
    }

    private void reducingRows(Matrix left, Matrix right, int column, int index) {
        double tempValue1 = left.getElement(index, column);
        for (int col = 0; col < left.columnCount(); col++)
            left.setElement(index, col, MyFunctions.roundToZero(left.getElement(index, col) / tempValue1));
        for (int col = 0; col < right.columnCount(); col++)
            right.setElement(index, col, MyFunctions.roundToZero(right.getElement(index, col) / tempValue1));
        for (int row = 0; row < left.rowCount(); row++) {
            double tempValue2 = left.getElement(row, column);
            for (int col = 0; row != index & col < left.columnCount(); col++)
                left.setElement(row, col, MyFunctions.roundToZero(left.getElement(row, col) - left.getElement(index, col) * tempValue2));
            for (int col = 0; row != index & col < right.columnCount(); col++)
                right.setElement(row, col, MyFunctions.roundToZero(right.getElement(row, col) - right.getElement(index, col) * tempValue2));
        }
    }

    private void rearrangingRows(Matrix left, Matrix right, int[] leadingRows) {
        for (int row = 0; row < left.rowCount(); row++) {
            for (int col1 = row; col1 < left.columnCount(); col1++) {
                int index = getIndex(leadingRows, col1);
                if (index != -1 && index >= row) {
                    int temp1 = leadingRows[row];
                    leadingRows[row] = leadingRows[index];
                    leadingRows[index] = temp1;
                    for (int col2 = 0; col2 < left.columnCount(); col2++) {
                        double temp2 = left.getElement(index, col2);
                        left.setElement(index, col2, left.getElement(row, col2));
                        left.setElement(row, col2, temp2);
                    }
                    for (int col2 = 0; col2 < right.columnCount(); col2++) {
                        double temp2 = right.getElement(index, col2);
                        right.setElement(index, col2, right.getElement(row, col2));
                        right.setElement(row, col2, temp2);
                    }
                    break;
                }
            }
        }
    }

    public Matrix getLeft() {
        return left;
    }

    public Matrix getRight() {
        return right;
    }

    public int[] getLeadingRows() {
        return leadingRows;
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
        var system = getFullyReducedEchelonForm(this);
        for (int i = 0; i < system.left.rowCount(); i++)
            if (system.left.getRowMatrix(i).isZeroMatrix() && !system.right.getRowMatrix(i).isZeroMatrix())
                return false;
        return true;
    }

    private Solution solution() {
        var system = getFullyReducedEchelonForm(this);
        if (!system.isConsistentSystem())
            return null;
        boolean hasUnique = !system.right.getColumnMatrix(0).isZeroMatrix();
        Matrix[] matrices = new Matrix[system.left.columnCount() - leadingRowCount + (hasUnique ? 1 : 0)];
        int index = 0;
        for (int i = 0; i < system.left.columnCount(); i++) {
            if (getIndex(system.leadingRows, i) == -1) {
                matrices[index] = copyMatrix(Matrix.scalarProduct(system.left.getColumnMatrix(i), -1));
                matrices[index++].setElement(i, 0, 1);
            }
        }
        if (hasUnique) matrices[index] = copyMatrix(system.right.getColumnMatrix(0));
        return new Solution(hasUnique, matrices);
    }

    private AugmentedMatrix getFullyReducedEchelonForm(AugmentedMatrix system) {
        return system.left.isFullyReducedEchelonForm() ? system : reducedEchelonForm();
    }

    private Matrix copyMatrix(Matrix matrix) {
        var resulting = Matrix.generateZeroMatrix(left.columnCount(), 1);
        for (int i = 0; i < left.rowCount() && i < left.columnCount(); i++)
            resulting.setElement(i, 0, matrix.getElement(i, 0));
        return resulting;
    }

    public boolean isTrivialSolution() {
        var sol = solution();
        return sol != null && sol.hasUnique && sol.matrices.length == 1 && sol.matrices[0].isZeroMatrix();
    }

    public int getRank() {
        var system = getFullyReducedEchelonForm(this);
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
