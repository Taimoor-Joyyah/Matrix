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
                {-2, 5},
                {4, -3},
                {2, 1}
        });
        var matrixE = new Matrix(new double[][]{
                {1, 2, -3, 4},
                {-4, 2, 1, 3},
                {3, 0, 0, -3},
                {2, 0, -2, 3}
        });
        var matrixF = new Matrix(new double[][]{
                {3, -2, 1, 8},
                {5, 6, 2, 2},
                {1, 0, -3, 5},
                {3, 0, 2, 1}
        });

        System.out.println("Dot Product :-\n" + Matrix.dotProduct(matrixA, matrixB));
        System.out.println("Matrix Product :-\n" + Matrix.matrixProduct(matrixC, matrixD));
        System.out.println("Get Submatrix :-\n" + matrixC.subMatrix(0, 1, 2, 2));
        System.out.println("Determinant :-\n" + matrixF.determinant());
        System.out.println("Adjoint :-\n" + matrixF.adjoint());
        System.out.println("Inverse :-\n" + matrixF.inverse());
        System.out.println("Inverse inverse :-\n" + matrixF.inverse().inverse());
        System.out.println("IsSquare :-\n" + matrixB.isSquareMatrix());
        System.out.println("Identity 4 :-\n" + Matrix.generateIdentityMatrix(4));
    }
}