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
    private int index = 0;
    double cameraX = 0.5;
    double cameraY = -1.5;
    double cameraZ = 1;
    List<Solid> solids = new ArrayList<>();
    private int mouseX, mouseY;
    boolean projection = true;
    private Col highlightColor = new Col(0xffffff);
    public Solid xAxis, yAxis, zAxis;
    public Solid pyramid, cube, selectedSolid;
    public Curve curve;
    public CubicCurve cubicCurve;
    private Camera camera;
    private Mat4PerspRH proj;
    private Mat4OrthoRH proj2;
    private Mat4 modelPyramid, modelCube, modelCurve, modelCubicCurve;
    private javax.swing.Timer rotationTimer;
    private boolean rotating = false;



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

        // Pyramid
        pyramid = new Pyramid();
        modelPyramid = new Mat4RotZ(Math.toRadians(45))
                .mul(new Mat4Transl(0.4,0.2,0));
        pyramid.setModel(modelPyramid);
        solids.add(pyramid);

        // Cube
        cube = new Cube();
        modelCube = new Mat4Transl(0.8,0.2,0);
        cube.setModel(modelCube);
        solids.add(cube);

        // Curve
        curve = new Curve();
        modelCurve = new Mat4RotZ(Math.toRadians(90))
                .mul(new Mat4Transl(-0.5,0,0));
        curve.setModel(modelCurve);
        solids.add(curve);

        // Cubic curve
        cubicCurve = new CubicCurve();
        modelCubicCurve = new Mat4Identity();
        cubicCurve.setModel(modelCubicCurve);
        solids.add(cubicCurve);

        rotationTimer = new javax.swing.Timer(16, e -> {
            rotate(rotating);
            repaint();
        });


        initListeners();
        repaint();
        rotationTimer.start();
    }


    private void repaint() {
        panel.clear();

        if (projection) {
        renderer.setView(camera.getViewMatrix());
        renderer.setProj(proj);
        } else if (!projection) {
            renderer.setView(camera.getViewMatrix());
            renderer.setProj(proj2);
        }

        selectedSolid = solids.get(index);

        renderer.renderSolid(xAxis);
        renderer.renderSolid(yAxis);
        renderer.renderSolid(zAxis);
        for (Solid solid : solids) {
            if (solid == selectedSolid) {
                solid.setColor(highlightColor);
            } else {
                solid.setColor(solid.getOriginalColor());
            }
            renderer.renderSolid(solid);
        }
        panel.repaint();
    }

    private void initListeners() {

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Scale
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    scale(1.1);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    scale(0.9);
                }
                // Výběr aktivního tělesa
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if (index > 0) {
                        selectedSolid = solids.get(index--);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (index < solids.size() - 1) {
                        selectedSolid = solids.get(index++);
                    }
                }
                // Pohyb kamery
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    camera = camera.forward(0.05);
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    camera = camera.backward(0.05);
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    camera = camera.left(0.05);
                }
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    camera = camera.right(0.05);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    camera = camera.up(0.05);
                }
                if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    camera = camera.down(0.05);
                }
                // Změna projekce
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    projection = !projection;
                }
                // Rotace těles
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    rotating = !rotating;
                }
                // Přesnost vykreslení křivky
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    if (curve.getSegments() > 1 || cubicCurve.getSegments() > 1) {
                    curve.setSegments(curve.getSegments()/2);
                    cubicCurve.setSegments(cubicCurve.getSegments()/2);
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    curve.setSegments(curve.getSegments()*2);
                    cubicCurve.setSegments(cubicCurve.getSegments()*2);
                }
                // Reset kamery
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    camera = new Camera().withPosition(new Vec3D(cameraX,cameraY,cameraZ))
                            .addAzimuth(Math.toRadians(90))
                            .withZenith(Math.toRadians(-25))
                            .withFirstPerson(true);
                    scale(0);
                }
                // Nastavení kubiky křivky
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    cubicCurve.setCubic(new Cubic(Cubic.COONS, cubicCurve.getPoints()));
                }
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    cubicCurve.setCubic(new Cubic(Cubic.BEZIER, cubicCurve.getPoints()));
                }
                if (e.getKeyCode() == KeyEvent.VK_F) {
                    cubicCurve.setCubic(new Cubic(Cubic.FERGUSON, cubicCurve.getPoints()));
                }
                repaint();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
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

        panel.addMouseWheelListener(e -> {
            double scaleFactor = e.getPreciseWheelRotation() < 0 ? 1.1 : 0.9;
            scaleAll(scaleFactor);
        });

    }

    private void scale(double scale) {
        if (scale == 0) {
            xAxis.setModel(new Mat4Identity());
            yAxis.setModel(new Mat4Identity());
            zAxis.setModel(new Mat4Identity());
            cube.setModel(modelCube);
            pyramid.setModel(modelPyramid);
            curve.setModel(modelCurve);
            cubicCurve.setModel(modelCubicCurve);
        } else if (scale == 1) {
            return;
        } else {
            selectedSolid.setModel(selectedSolid.getModel().mul(new Mat4Scale(scale)));
        }
        repaint();
    }

    private void scaleAll(double scale) {
        xAxis.setModel(xAxis.getModel().mul(new Mat4Scale(scale)));
        yAxis.setModel(yAxis.getModel().mul(new Mat4Scale(scale)));
        zAxis.setModel(zAxis.getModel().mul(new Mat4Scale(scale)));
        for (Solid solid : solids) {
            solid.setModel(solid.getModel().mul(new Mat4Scale(scale)));
        }
        repaint();
    }

    private void rotate(boolean rotating) {
        if (rotating) {
            double centerX = 0.2;
            double centerY = 0.2;
            double centerZ = 0.0;

            Mat4 translationToOrigin = new Mat4Transl(-centerX, -centerY, -centerZ);
            Mat4 rotation = new Mat4RotZ(Math.toRadians(2));
            Mat4 translationBack = new Mat4Transl(centerX, centerY, centerZ);

            pyramid.setModel(translationToOrigin.mul(rotation).mul(translationBack).mul(pyramid.getModel()));
            cube.setModel(translationToOrigin.mul(rotation).mul(translationBack).mul(cube.getModel()));
        }
        repaint();
    }



}
