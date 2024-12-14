package solid;

import transforms.Col;
import transforms.Point3D;

public class Cube extends Solid{
    public Cube() {

        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0, .4,0));
        vb.add(new Point3D(.4, .4,0));
        vb.add(new Point3D(.4,.0,0));
        vb.add(new Point3D(0,0,0.4));
        vb.add(new Point3D(0, .4,0.4));
        vb.add(new Point3D(.4, .4,0.4));
        vb.add(new Point3D(.4,.0,0.4));

        addIndices(
                //spodní čtverec
                0,1,
                1,2,
                2,3,
                3,0,
                //horní čtverec
                4,5,
                5,6,
                6,7,
                7,4,
                //vertikální spojnice
                0,4,
                1,5,
                2,6,
                3,7

        );

        setOriginalColor(new Col(0xFFA500));
        setColor(getOriginalColor());


    }
}
