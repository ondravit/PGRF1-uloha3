package solid;

import transforms.Col;
import transforms.Cubic;
import transforms.Point3D;

public class CubicCurve extends Solid {

    private Cubic cubic;
    private Point3D a, b, c, d;
    private int segments = 50;

    public CubicCurve() {
        a  = new Point3D(-0.5,0,0);
        b  = new Point3D(-0.25,-0.5,0);
        c  = new Point3D(0.25,-0.5,0);
        d  = new Point3D(0.5,0,0);

        cubic = new Cubic(Cubic.BEZIER, a, b, c, d);

        computeVertices();

        setOriginalColor(new Col(0xFF1493));
        setColor(getOriginalColor());
    }
    public void setCubic(Cubic cubic) {
        this.cubic = cubic;
        computeVertices();
    }

    private void computeVertices() {
        vb.clear();
        ib.clear();

        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            Point3D p = cubic.compute(t);

            vb.add(p);
            if (i != segments) {
                ib.add(i);
                ib.add(i + 1);
            }
        }
    }

    public Point3D[] getPoints() {
        return new Point3D[] {a, b, c, d};
    }

    public void setSegments(int segments) {
        this.segments = segments;
        computeVertices();
    }

    public int getSegments() {
        return segments;
    }
}
