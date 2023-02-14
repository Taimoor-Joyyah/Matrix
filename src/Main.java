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

        var matrixfA = new FractionMatrix(new Fraction[][]{
                {new Fraction(3,4),new Fraction(6,4),new Fraction(7,8)},
                {new Fraction(4,7),new Fraction(-3,1),new Fraction(2,1)},
                {new Fraction(2,5),new Fraction(0,1),new Fraction(-3,2)}
        });

//        System.out.println(matrixF.isFullyReducedEchelonForm());
//        var rr = matrixF.getReducedRowEchelon();
//        System.out.println(rr);
//        System.out.println(rr.isReducedEchelonForm());
//        System.out.println(rr.isFullyReducedEchelonForm());

//        System.out.println(matrixfA);
//        System.out.println(matrixfA.inverse());
//        System.out.println(matrixfA.inverse().inverse());



        Matrix.setDisplayAttributes(false, true, 8, 12);

//        AugmentedMatrix system = new AugmentedMatrix(matrixE, matrixH);
//        System.out.println(AugmentedMatrix.solveX(matrixE, matrixH));
//        system.reducedEchelonForm();
//        System.out.println(system);
        System.out.println(Vector.isLinearlyCombination(
                new Vector(-1,2,3),

                new Vector(4,2,-3),
                new Vector(2,1,-2),
                new Vector(-2,-1,0)
        ));
    }
}