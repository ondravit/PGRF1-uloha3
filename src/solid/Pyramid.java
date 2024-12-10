package solid;

import transforms.Col;
import transforms.Point3D;

public class Pyramid extends Solid{
    public Pyramid() {

        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0, .4,0));
        vb.add(new Point3D(.4, .4,0));
        vb.add(new Point3D(.4,.0,0));
        vb.add(new Point3D(.2,.2,.6));

        addIndices(
                0,1,
                1,2,
                2,3,
                3,0,
                4,0,
                4,1,
                4,2,
                4,3
        );

        setColor(new Col(0xffff00));


    }
}
