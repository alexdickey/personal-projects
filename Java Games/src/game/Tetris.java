package game;

import graphics.G;
import graphics.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Tetris extends Window implements ActionListener {

    public static Timer timer;
    // size of the well
    public static final int H = 20, W = 10, C = 25;
    public static final int xM = 50, yM = 50;

    public static Color[] colors = {Color.red, Color.green, Color.blue, Color.orange,
                                    Color.CYAN, Color.yellow, Color.magenta, Color.black, Color.PINK}; // shapes;

    public static Shape[] shapes = {Shape.Z, Shape.S, Shape.J, Shape.L, Shape.I, Shape.O,
                                    Shape.T};
    public static int[][] well = new int [W][H]; // grid of "Dead" shapes
    public static Shape shape;
    public static int iBack = 7; // index of background color
    public static int zap = 8; // index pf empty space

    public static int time = 1, iShape = 0;

    public Tetris() {
        super("Tetris", 1000, 700); // super must be the first thing in the constructor;
        shape = shapes[G.rnd(7)]; // get a random single shape
        clearWell();
        timer = new Timer(30, this);
        timer.start();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        time ++;
        if (time == 10){
            time = 0;
            shape.drop();
        }
        unZapWell();
        showWell(g);
        shape.show(g);

//        if (time == 60){time = 0; iShape = (iShape + 1) % 7;}
//        if (time == 30){
//            shapes[iShape].rot();
//        }
//        shapes[iShape].show(g);
    }

    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if (vk == KeyEvent.VK_LEFT){shape.slide(G.LEFT);}
        if (vk == KeyEvent.VK_RIGHT){shape.slide(G.RIGHT);}
        if (vk == KeyEvent.VK_UP){shape.safeRot();}
        if (vk == KeyEvent.VK_DOWN){shape.drop();}
        repaint();
    }

    //clear well
    public static void clearWell() {
        for (int x = 0; x < W; x ++){
            for (int y = 0; y < H; y ++){
                well[x][y] = iBack;
            }
        }
    }

    //show well
    public static void showWell(Graphics g){
        for (int x = 0; x < W; x ++){
            for (int y = 0; y < H; y ++){
                g.setColor(colors[well[x][y]]);
                int xx = xM + C * x, yy = yM + C * y;
                g.fillRect(xx, yy, C, C);
                g.setColor(Color.black);
                g.drawRect(xx, yy, C, C);

            }
        }
    }

    public static void zapWell (){
        for (int y = 0; y < H; y ++){
            zapRow(y);
        }
    }

    private static void zapRow(int y) {
        for (int x = 0; x < W; x ++){
            if (well[x][y] == iBack){
                return; // if there is any background, no zap
            }
        }
        for (int x = 0; x < W; x ++){
            well[x][y] = zap; // if it makes it through
        }
    }

    public static void unZapWell(){
        boolean done = false;
        for(int y = 1; y < H; y ++){
            for (int x = 0; x < W; x ++){
                if (well[x][y - 1] != zap && well[x][y] == zap){ // non zap sitting on a zap
                    done = true;
                    well[x][y] = well[x][y-1];
                    well[x][y-1] = (y == 1) ? iBack : zap;
                }

            }
            if (done){return;}
        }

    }

    public static void dropANewShape() {
        shape = shapes[G.rnd(7)];
        shape.loc.set(4,0); // new shape centered at the top;


    }

    public static void main (String[] args){
        (PANEL = new Tetris()).launch(); // Panel is a static variable in the window that contains the window.
    }
    //--------------------------------Shape--------------------------------//
    public static class Shape{
        public static Shape Z, S, J, L, I, O, T;
        public static G.V temp = new G.V(0, 0);
        public static Shape cds = new Shape(new int[]{0,0, 0,0, 0,0, 0,0}, 0);
        public G.V[] a = new G.V[4]; // array that holds the 4 squares for each shape
        public G.V loc = new G.V(); //location of the shape;
        public int iColor; // index of the colors array




        public Shape(int[] xy, int iC){ // list of x y coordinates
            for (int i = 0; i < 4; i ++){
                a[i] = new G.V();
                a[i].set(xy[i * 2], xy[i * 2 + 1]); //even index is the x index, odd index is the y index
            }
            iColor = iC;
        }
        public void  show(Graphics g){
            g.setColor(colors[iColor]);
            for (int i = 0; i < 4; i ++){
                g.fillRect(x(i), y(i),C, C); // interior color of the squares
            }
            g.setColor(Color.black);
            for (int i = 0; i < 4; i ++){
                g.drawRect(x(i), y(i),C, C); // draw the border of squares
            }
        }
        public int x(int i ){
            return xM + C * (a[i].x + loc.x);
        }
        public int y(int i ){
            return yM + C * (a[i].y + loc.y);
        }

        public void rot() {// rotation
            temp.set(0,0);
            for (int i = 0; i < 4; i ++){
                a[i].set(-a[i].y, a[i].x); // rotate the coordinate (x, y) --> (-y, x)  90 degree rotation
                if (temp.x > a[i].x){temp.x = a[i].x;} // keeping track of the min x & y
                if (temp.y > a[i].y){temp.y = a[i].y;} // making the temp that min
            }
            temp.set(-temp.x, -temp.y); // negate or 0'ing out a potentially neg component;
            for (int i = 0; i < 4 ; i ++) {
                a[i].add(temp);
            }
        }

        public void safeRot(){
            rot();
            cdsSet();
            if(collisionDetected()){
                rot();rot();rot();

            }
        }


        static {
            Z = new Shape(new int[]{0,0, 1,0, 1,1, 2,1}, 0);
            S = new Shape(new int[]{0,1, 1,0, 1,1, 2,0}, 1);
            J = new Shape(new int[]{0,0, 0,1, 1,1, 2,1}, 2);
            L = new Shape(new int[]{0,1, 1,1, 2,1, 2,0}, 3);
            I = new Shape(new int[]{0,0, 1,0, 2,0, 3,0}, 4);
            O = new Shape(new int[]{0,0, 1,0, 0,1, 1,1}, 5);
            T = new Shape(new int[]{0,1, 1,0, 1,1, 2,1}, 6);
        }

        public static boolean collisionDetected(){
            for (int i = 0; i < 4; i ++){ // make sure we are not outside the boundary;
                G.V v = cds.a[i]; // v is the local way
                if (v.x < 0 || v.x >= W || v.y < 0 || v.y >= H){ //
                    return true;
                }
                if(well[v.x][v.y] <= 6){ // seen collision
                    return true;
                }
            }
            return false;
        }

        public void cdsSet(){
            for (int i = 0; i < 4; i ++){
                cds.a[i].set(a[i]);// call on an existing shape // object --> cds.a[i]
                cds.a[i].add(loc);
            }
        }
        public void cdsGet(){
            for (int i = 0; i < 4; i ++){
                a[i].set(cds.a[i]);// call on an existing objects // cds.a[i] --> object
            }
        }
        public void cdsAdd(G.V v){
            for (int i = 0; i < 4; i ++){
                cds.a[i].add(v);// add vector to each element in cd
            }
        }

        public void slide(G.V dX) {
            cdsSet();
            cds.cdsAdd(dX);
            if (collisionDetected()){
                return;
            }
//            cdsGet(); //copy back to the shape
            loc.add(dX); // slide is updating loc rather than changing it

        }

        public void drop() {
            cdsSet();
            cds.cdsAdd(G.DOWN);
            if (collisionDetected()){
                copyToWell();
                zapWell();

                dropANewShape();
                return;
            }
            loc.add(G.DOWN);

        }

        public void copyToWell() {
            for (int i = 0; i < 4; i ++){
                well[a[i].x + loc.x][a[i].y + loc.y] = iColor; // this shape has a color thats how we copy the shape in the well
            }
        }
    }
}
