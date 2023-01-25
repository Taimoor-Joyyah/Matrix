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

        System.out.println("Dot Product :-\n" + Matrix.dotProduct(matrixA, matrixB));
        System.out.println("Matrix Product :-\n" + Matrix.matrixProduct(matrixC, matrixD));
        System.out.println("Get Column :-\n" + matrixD.getColumn(1));
        System.out.println("Get Row :-\n" + matrixD.getRow(1));
        System.out.println("IsZero :-\n" + matrixB.isZeroMatrix());
        System.out.println("IsSquare :-\n" + matrixB.isSquareMatrix());
        System.out.println("Identity 4 :-\n" + Matrix.generateIdentity(4));
    }
}