package render;

import model.Line;
import rasterize.LineRasterizer;
import solid.Solid;
import transforms.Point3D;
import transforms.Vec3D;

import java.util.ArrayList;
import java.util.List;

public class Renderer {

    private LineRasterizer lineRasterizer;
    private int width, height;

    public Renderer(LineRasterizer lineRasterizer, int width, int height) {
        this.lineRasterizer = lineRasterizer;
        this.width = width;
        this.height = height;
    }

    public void renderSolid(Solid solid) {
        // TODO:
        // Projít všechny vrcholy, pronásobit je modelovací maticí a uložit do nového seznamu, ze kterého
        // je pak bude další metoda brát
        List<Point3D> vertices = new ArrayList<>();
        for (int i = 0; i < solid.getVb().size(); i++) {
            vertices.add(solid.getVb().get(i).mul(solid.getModel()));
        }


        // Prochazeni IB
        for (int i = 0; i < solid.getIb().size(); i += 2) {
            int indexP1 = solid.getIb().get(i);
            int indexP2 = solid.getIb().get(i + 1);

            Point3D p1 = solid.getVb().get(indexP1);
            Point3D p2 = solid.getVb().get(indexP2);

            // Násobení vrcholů modelovací maticí
            p1 = p1.mul(solid.getModel());
            p2 = p2.mul(solid.getModel());

            // Transformace do okna obrazovky
            Vec3D p1atScreen = transformToScreen(new Vec3D(p1));
            Vec3D p2atScreen = transformToScreen(new Vec3D(p2));

            Line line = new Line(
                    (int) Math.round(p1atScreen.getX()),
                    (int) Math.round(p1atScreen.getY()),
                    (int) Math.round(p2atScreen.getX()),
                    (int) Math.round(p2atScreen.getY())
            );

            lineRasterizer.setColor(solid.getColor().getRGB());
            lineRasterizer.drawLine(line);
        }
    }

    public void renderSolids(List<Solid> solids) {
        for (Solid solid : solids) {
            renderSolid(solid);
        }
    }

    public void setLineRasterizer(LineRasterizer lineRasterizer) {
        this.lineRasterizer = lineRasterizer;
    }

    private Vec3D transformToScreen(Vec3D point) {
        return point.mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1,1,0))
                .mul(new Vec3D((width - 1) / 2., (height - 1) / 2., 1));
    }
}
