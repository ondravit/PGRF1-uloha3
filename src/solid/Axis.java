package solid;

import transforms.Point3D;

public class Axis extends Solid{
    public Axis() {

        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0.95,0,0));
        vb.add(new Point3D(0.95,-0.05,0));
        vb.add(new Point3D(1,0,0));
        vb.add(new Point3D(0.95,0.05,0));

        addIndices(
                0,1,
                1,2,
                2,3,
                3,4,
                4,1
        );
    }
}
