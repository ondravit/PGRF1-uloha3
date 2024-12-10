package controller;

import rasterize.LineRasterizerGraphics;
import render.Renderer;
import solid.*;
import transforms.*;
import view.Panel;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {
    private final Panel panel;
    private Renderer renderer;
    int deg = 0;
    double cameraX = 0.5;
    double cameraY = -1.5;
    double cameraZ = 1;
    List<Solid> solids = new ArrayList<>();
    int mouseX, mouseY;
    boolean projection = true;


    public Solid xAxis, yAxis, zAxis;
    public Solid arrow, pyramid, cube;

    private Camera camera;
    private Mat4PerspRH proj;
    private Mat4OrthoRH proj2;

    public Controller3D(Panel panel) {
        this.panel = panel;

        renderer = new Renderer(new LineRasterizerGraphics(panel.getRaster()), panel.getWidth(), panel.getHeight());

        // Kamera pozorovatele
        camera = new Camera()
                .withPosition(new Vec3D(cameraX,cameraY,cameraZ))
                .addAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-25))
                .withFirstPerson(true);

        // Projekční matice - perspektivní
        proj = new Mat4PerspRH(
                Math.toRadians(60),
                panel.getRaster().getHeight()/(float)panel.getRaster().getWidth(),
                0.01,
                200
        );

        // Projekční matice - pravoúhlá
        proj2 = new Mat4OrthoRH(
                panel.getRaster().getWidth()/(float)200,
                panel.getRaster().getHeight()/(float)200,
                0.01,
                200);



        // Souřadnicové osy
        xAxis = new AxisX();
        yAxis = new AxisY();
        zAxis = new AxisZ();
        xAxis.setModel(xAxis.getModel());
        yAxis.setModel(yAxis.getModel());
        zAxis.setModel(zAxis.getModel());
        solids.add(xAxis);
        solids.add(yAxis);
        solids.add(zAxis);

        // Arrow
        /*
        arrow = new Arrow();
        Mat4 modelAr = new Mat4Transl(-0.5,0,0)
                .mul(new Mat4RotZ(Math.toRadians(0)))
                .mul(new Mat4Transl(0.5,0,0));
        arrow.setModel(modelAr);
        solids.add(arrow);

         */

        // Pyramid
        pyramid = new Pyramid();
        Mat4 modelPy = new Mat4RotZ(Math.toRadians(45))
                .mul(new Mat4Transl(0.4,0.2,0));
        pyramid.setModel(modelPy);
        solids.add(pyramid);

        // Cube
        cube = new Cube();
        Mat4 modelCu = new Mat4Transl(0.8,0.2,0);
        cube.setModel(modelCu);
        solids.add(cube);



        initListeners();
        repaint();
    }

    private void repaint() {
        panel.clear();

        // TODO: set somewhere else in the code
        if (projection) {
        renderer.setView(camera.getViewMatrix());
        renderer.setProj(proj);
        } else if (!projection) {
            renderer.setView(camera.getViewMatrix());
            renderer.setProj(proj2);
        }


        renderer.renderSolids(solids);
        panel.repaint();
    }

    private void initListeners() {

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    scale(1.1);

                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    scale(0.9);

                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.move(new Vec3D(0,0.05,0));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.move(new Vec3D(0,-0.05,0));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.move(new Vec3D(-0.05,0,0));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.move(new Vec3D(0.05,0,0));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    camera = camera.move(new Vec3D(0, 0, 0.05));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    camera = camera.move(new Vec3D(0, 0, -0.05));
                    repaint();
                }
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    projection = !projection;
                    repaint();
                }
                // Reset kamery
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    camera = new Camera().withPosition(new Vec3D(cameraX,cameraY,cameraZ))
                            .addAzimuth(Math.toRadians(90))
                            .withZenith(Math.toRadians(-25))
                            .withFirstPerson(true);
                    repaint();
                }
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Set the initial position when the mouse is pressed
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });


        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - mouseX;
                double dy = e.getY() - mouseY;
                camera = camera.addAzimuth(Math.toRadians(dx * 0.1))
                        .addZenith(Math.toRadians(dy * 0.1));
                repaint();
                mouseX = e.getX();
                mouseY = e.getY();
            }

        });

    }

    private void scale(double scale) {
        for (Solid solid : solids) {
            solid.setModel(solid.getModel().mul(new Mat4Scale(scale)));
        }
    }
}
