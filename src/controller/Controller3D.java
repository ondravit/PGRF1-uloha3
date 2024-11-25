package controller;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import render.Renderer;
import solid.Arrow;
import solid.Solid;
import view.Panel;

public class Controller3D {
    private final Panel panel;
    private Renderer renderer;

    public Solid arrow;

    public Controller3D(Panel panel) {
        this.panel = panel;

        renderer = new Renderer(new LineRasterizerGraphics(panel.getRaster()), panel.getWidth(), panel.getHeight());

        arrow = new Arrow();

        initListeners();
        repaint();
    }

    private void repaint() {
        renderer.renderSolid(arrow);
        panel.repaint();
    }

    private void initListeners() {

    }
}
