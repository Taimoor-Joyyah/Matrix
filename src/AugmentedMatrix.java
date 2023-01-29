import java.util.Arrays;

public class AugmentedMatrix {
    private final Matrix left;
    private final Matrix right;

    public AugmentedMatrix(Matrix left, Matrix right) {
        if (left == null || right == null)
            throw new IllegalArgumentException("Matrix should not be null!");
        if (left.rowCount() != right.rowCount())
            throw new IllegalArgumentException("Both matrices should have equal amount of rows!");
        this.left = left.deepClone();
        this.right = right.deepClone();
    }

    public static Matrix solveX(Matrix matrixA, Matrix matrixB) {
        return Matrix.matrixProduct(matrixA.inverse(), matrixB);
    }

    public void reducedEchelonForm() {
        int[] leadingRows = new int[left.rowCount()];
        int leadingRowCount = 0;
        Arrays.fill(leadingRows, -1);
        for (int col1 = 0; col1 < left.columnCount() && leadingRowCount <= left.rowCount(); col1++) {
            int index = -1;
            for (int row = 0; row < left.rowCount(); row++) {
                if (left.getElement(row, col1) != 0 && leadingRows[row] == -1) {
                    leadingRows[row] = col1;
                    ++leadingRowCount;
                    index = row;
                    break;
                }
            }
            if (index == -1)
                continue;
            reducingRows(col1, index);
        }
        rearrangingRows(leadingRows);
    }

    private void reducingRows(int column, int index) {
        double tempValue1 = left.getElement(index, column);
        for (int col = 0; col < left.columnCount(); col++)
            left.setElement(index, col, left.getElement(index, col) / tempValue1);
        for (int col = 0; col < right.columnCount(); col++)
            right.setElement(index, col, right.getElement(index, col) / tempValue1);
        for (int row = 0; row < left.rowCount(); row++) {
            double tempValue2 = left.getElement(row, column);
            for (int col = 0; row != index & col < left.columnCount(); col++)
                left.setElement(row, col, left.getElement(row, col) - left.getElement(index, col) * tempValue2);
            for (int col = 0; row != index & col < right.columnCount(); col++)
                right.setElement(row, col, right.getElement(row, col) - right.getElement(index, col) * tempValue2);
        }
    }

    private void rearrangingRows(int[] leadingRows) {
        for (int row1 = 0; row1 < left.rowCount(); row1++) {
            for (int col1 = row1; col1 < left.columnCount(); col1++) {
                int index = getIndex(leadingRows, col1);
                if (index != -1 && index >= row1) {
                    int temp1 = leadingRows[row1];
                    leadingRows[row1] = leadingRows[index];
                    leadingRows[index] = temp1;
                    for (int col2 = 0; col2 < left.columnCount(); col2++) {
                        double temp2 = left.getElement(index, col2);
                        left.setElement(index, col2, left.getElement(row1, col2));
                        left.setElement(row1, col2, temp2);
                    }
                    for (int col2 = 0; col2 < right.columnCount(); col2++) {
                        double temp2 = right.getElement(index, col2);
                        right.setElement(index, col2, right.getElement(row1, col2));
                        right.setElement(row1, col2, temp2);
                    }
                    break;
                }
            }
        }
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

    @Override
    public String toString() {
        return left + "\n" + right;
    }
}
