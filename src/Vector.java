public class Vector {
    private final Matrix matrix;

    public Vector(double... components) {
        matrix = new Matrix(new double[components.length][1]);

        for (int i = 0; i < components.length; i++)
            setComponent(i, components[i]);
    }

    public Vector(Matrix matrix) {
        if (matrix.columnCount() != 1)
            throw new IllegalArgumentException("Vector Should be Column Matrix!");
        this.matrix = matrix;
    }

    public double getComponent(int component) {
        return matrix.getElement(component, 1);
    }

    public void setComponent(int component, double value) {
        matrix.setElement(component, 0, value);
    }

    public int getDimension() {
        return matrix.rowCount();
    }

    public static Vector Addition(Vector left, Vector right) {
        return new Vector(Matrix.addition(left.matrix, right.matrix));
    }

    public static Vector subtraction(Vector left, Vector right) {
        return new Vector(Matrix.subtraction(left.matrix, right.matrix));
    }

    public static Vector scalarMultiplication(Vector vector, int scale) {
        return new Vector(Matrix.scalarProduct(vector.matrix, scale));
    }

    public static double dotProduct(Vector left, Vector right) {
        return Matrix.dotProduct(left.matrix, right.matrix);
    }

    public static Vector crossProduct(Vector left, Vector right) {
        return new Vector(Matrix.matrixProduct(left.matrix, right.matrix));
    }

    public double magnitude() {
        return Math.sqrt(dotProduct(this, this));
    }

    public static Vector unitVector(Vector vector) {
        return new Vector(Matrix.scalarProduct(vector.matrix, 1 / vector.magnitude()));
    }

    public static boolean isLinearlyDependent(Vector... vectors) {
        return isLinearlyCombination(new Vector(Matrix.generateZeroMatrix(vectors[0].getDimension(), 1)), vectors);
    }

    public static boolean isLinearlyCombination(Vector v, Vector... vectors) {
        var matrices = new Matrix[vectors.length];
        for (int i = 0; i < vectors.length; i++)
            matrices[i] = vectors[i].matrix;
        var system = new AugmentedMatrix(Matrix.combineMatrix(matrices), v.matrix);
        if (!system.getLeft().isReducedEchelonForm())
            system = system.reducedEchelonForm();
        return !system.isTrivialSolution();
    }

    public boolean isZeroVector() {
        return matrix.isZeroMatrix();
    }

    public boolean isUnitVector() {
        return magnitude() == 1;
    }

    public Vector deepClone() {
        return new Vector(matrix.deepClone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return matrix.equals(vector.matrix);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("(");
        for (int i = 0; i < getDimension(); i++) {
            builder.append(getComponent(i));
            if (i + 1 < getDimension())
                builder.append(",");
            else
                builder.append(")");
        }
        return builder.toString();
    }
}
