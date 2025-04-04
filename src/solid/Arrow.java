package solid;

import transforms.Col;
import transforms.Point3D;

import java.util.ArrayList;

public class Arrow extends Solid{
    public Arrow() {

        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0.8, 0,0));
        vb.add(new Point3D(1,0,0));
        vb.add(new Point3D(0.8, .1,0));
        vb.add(new Point3D(0.8,-.1,0));

        addIndices(
                0,1,
                1,4,
                4,2,
                2,3,
                3,1
        );


    }
}
