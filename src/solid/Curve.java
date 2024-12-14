package solid;

import transforms.Col;
import transforms.Point3D;

public class Curve extends Solid {
    private int segments = 50;
    public Curve() {

        computeVertices();

        setOriginalColor(new Col(0x40e0d0));
        setColor(getOriginalColor());
    }

    private void computeVertices() {
        vb.clear();
        ib.clear();
        for (int i = 0; i <= segments; i++) {
            float x = (i / (float) segments); // * 2 - 1 ;
            float y = (float)Math.sin(4 * Math.PI * x)/2;

            vb.add(new Point3D(x, y, 0));
            if (i != segments) {
                ib.add(i);
                ib.add(i+1);
            }
        }
    }

    public void setSegments(int segments) {
        this.segments = segments;
        computeVertices();
    }
    public int getSegments() {
        return segments;
    }
}
