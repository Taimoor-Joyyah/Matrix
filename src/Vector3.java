public class Vector3 extends Vector {
    public Vector3(double x, double y, double z) {
        super(x, y, z);
    }

    public double x() {
        return getComponent(0);
    }

    public double y() {
        return getComponent(1);
    }

    public double z() {
        return getComponent(2);
    }
}
