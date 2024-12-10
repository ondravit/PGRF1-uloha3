package solid;

import transforms.Col;
import transforms.Point3D;

public class AxisZ extends Solid{
    public AxisZ() {

        vb.add(new Point3D(0,0,0));
        vb.add(new Point3D(0,0,1));

        addIndices(0,1);

        setColor(new Col(0x0000ff));
    }
}
