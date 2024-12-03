package controller;

import rasterize.LineRasterizer;
import rasterize.LineRasterizerGraphics;
import render.Renderer;
import solid.Arrow;
import solid.Axis;
import solid.Solid;
import transforms.*;
import view.Panel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller3D {
    private final Panel panel;
    private Renderer renderer;
    int deg = 0;

    public Solid arrow;
    public Solid xAxis;
    public Solid yAxis;

    public Controller3D(Panel panel) {
        this.panel = panel;

        renderer = new Renderer(new LineRasterizerGraphics(panel.getRaster()), panel.getWidth(), panel.getHeight());


        arrow = new Arrow(); // Vytvoření instance šipky
        xAxis = new Axis();
        xAxis.setColor(new Col(0xff0000));
        yAxis = new Axis();
        yAxis.setModel(new Mat4RotZ(Math.toRadians(90)));
        yAxis.setColor(new Col(0x00ff00));



        initListeners();
        repaint();
    }

    private void repaint() {
        panel.clear();

        Mat4 model = new Mat4Transl(-0.5,0,0)
                .mul(new Mat4RotZ(Math.toRadians(deg)))
                .mul(new Mat4Transl(0.5,0,0));
        arrow.setModel(model);

        renderer.renderSolid(arrow);
        renderer.renderSolid(xAxis);
        renderer.renderSolid(yAxis);
        panel.repaint();
    }

    private void initListeners() {

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    deg += 10;

                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    deg -= 10;

                    repaint();
                }
            }
        });
    }
}
