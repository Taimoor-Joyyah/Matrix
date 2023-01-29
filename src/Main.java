public class Main {
    public static void main(String[] args) {
        var matrixA = new Matrix(new double[][]{
                {4, 3, 0},
                {3, 6, 2}
        });
        var matrixB = new Matrix(new double[][]{
                {5, 6, 7},
                {3, 2, 1}
        });
        var matrixC = new Matrix(new double[][]{
                {1, 2, -1},
                {3, 1, 4}
        });
        var matrixD = new Matrix(new double[][]{
                {1, -3, 3, 0},
                {0, 1, 1, 4},
                {0, 0, 1, 1},
                {0, 0, 0, 1}
        });
        var matrixE = new Matrix(new double[][]{
                {1, 1, 2, -1},
                {1, 2, 1, 0},
                {-1, -4, 1, -2},
                {1, -2, 5, -4}
        });
        var matrixF = new Matrix(new double[][]{
                {4, -7, 3, 8, 7, 5, 2, 5},
                {3, 7, 3, -8, -9, 5, 4, 1},
                {2, 9, -2, 9, -4, 0, -2, 7},
                {-8, 7, 9, 8, 3, -4, 6, -4},
                {6, 8, 0, -5, 7, 0, 8, 0},
                {9, 2, 0, 8, 9, 3, 1, 5},
                {2, 4, 0, -3, -3, 0, -2, 0},
                {1, 7, -1, 2, 6, -4, 2, 4},
        });

        var matrixG = new Matrix(new double[][]{
                {1, 2, 3, 8, 9, 0},
                {7, 1, 3, 7, 3, 5},
                {3, 0, 3, 4, 9, 4},
                {5, 8, 3, 8, 5, 3},
                {1, 2, 0, 5, 4, 9},
                {9, 0, 3, 8, 6, 0},
        });
        var matrixH = new Matrix(new double[][]{
                {6},
                {14},
                {-2},
                {-7},
        });

        Matrix.setDisplayAttributes(false, true, 8, 12);

        AugmentedMatrix matrix = new AugmentedMatrix(matrixE, matrixH);
//        System.out.println(AugmentedMatrix.solveX(matrixE, matrixH));
        matrix.reducedEchelonForm();
        System.out.println(matrix);
    }
}