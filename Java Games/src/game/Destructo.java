package game;

import graphics.G;
import graphics.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class Destructo extends Window implements ActionListener {

    public static Timer timer;
    public Grid grid = new Grid();
    public Destructo() {
        super("Destruto", 1000, 700);
        timer = new Timer(30,this); // 30 times per second
        timer.start();
    }

    public static void main(String[] args){
        (PANEL = new Destructo()).launch();
    }

    @Override
    public void paintComponent(Graphics g){
        G.clear(g);
        grid.show(g);
        g.setColor(Color.black);
        g.drawString("REPLAY", 10, 20);
        String msg = "Bricks Remaining: "+ grid.bricksRemaining;
        msg += grid.noMoreMoves() ? "Game Over :)" : "";
        g.drawString(msg,  600, 30);

    }

    public void mouseClicked(MouseEvent me){
        int x = me.getX(), y = me.getY();
        if (x < grid.xM / 2 && y < grid.yM / 2){
            replay();
        }
        if (grid.contains(x, y)){
            grid.action();
            repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        grid.bubbleSort();
        if(grid.slideCol()){
            grid.xM += grid.W / 2;

        }
        repaint();
    }

    public void replay(){
        grid.rndColors(G.rnd(3)  + 3);
        grid.bricksRemaining = grid.nR * grid.nC;
        grid.xM = 100;
        grid.yM = 100;
    }


    //-----------------------Grid-------------------//
    public static class Grid {
        public static final int nR = 15, nC = 13, W = 50,H = 30;
        public  static int xM = 100, yM = 100, bricksRemaining = nR * nC;
        public static Color[] color = {Color.WHITE, Color.CYAN, Color.green, Color.YELLOW, Color.RED,
                                        Color.pink};
        public static int iBK = 0;
        int R,C; // set by contains(x, y).
        int [][] a = new int [nR][nC];

        public Grid() {
            rndColors(5);
        }

        public void rndColors(int k){ // k is the number of colors
            for (int r = 0; r < nR; r ++) {
                for (int c = 0; c < nC; c++) {
                    a[r][c] = G.rnd(k) + 1; // skips 0 (Light Grey)
                }
            }
        }

        public void show(Graphics g){
            for (int r = 0; r < nR; r ++) {
                for (int c = 0; c < nC; c++) {
                    g.setColor(color[a[r][c]]);
                    g.fillRect(x(c), y(r), W, H);
                }
            }
        }

        public int x(int c){
            return xM + c * W;
        }
        public int y(int r){
            return yM + r * H;
        }
        public int c(int x){
            return (x - xM) / W;
        }
        public int r(int y){
            return (y - yM) / H;
        }

        public boolean contains(int x, int y) {
            if (x < xM || y < yM) return false;
            R = r(y);
            C = c(x);
            if (R >= nR || C >= nC) return false;
            return true;
        }

        public void action() {
            if (infectable(R,C)){
                infect(R,C, a[R][C]);
                bubbleSort();
            }

        }
        public void infect(int r, int c, int v){ // v color used as guard
            if (a[r][c] != v) return;
            a[r][c] = iBK;
            bricksRemaining --;
            if (r > 0){infect(r - 1, c, v);}
            if (c > 0){infect(r, c - 1, v);}
            if (r < nR - 1){infect(r + 1, c, v);}
            if (c < nC - 1){infect(r, c + 1, v);}
        }

        public boolean infectable(int r, int c){
            int v = a[r][c];
            if (v == iBK){return false;}
            if (r > 0){if (a[r - 1][c] == v){return true;}}
            if (c > 0){if (a[r][c - 1] == v){return true;}}
            if (r < nR - 1){if (a[r + 1][c] == v){return true;}}
            if (c < nC - 1){if (a[r][c + 1]== v){return true;}}
            return false;
        }

        public boolean bubble(int c){ // true if we need to bubble again;
            boolean res = false;
            for (int r = nR - 1; r > 0; r --){
                if (a[r][c] == iBK && a[r-1][c] != iBK){ // if  we can swap, we can swap. the lower block can go up
                    a[r][c] = a[r-1][c];
                    a[r - 1][c] = iBK;
                    res = true;
//                    return res; // not going through the entire loop
                }
            }
            return res;
        }

        public void bubbleSort(){
            for (int c = 0; c < nC; c ++){
//                while (bubble(c)){
//                } // this bubble the entire colmn
                bubble(c);
            }
        }

        public boolean colIsEmpty(int c){
            for (int r = 0; r < nR; r ++){
                if (a[r][c] != iBK) return false;
            }
            return true;
        }

        public void swapCol(int c){ // c is the non-empty col and c - 1 is empty
            for (int r = 0; r < nR; r ++){
                a[r][c-1] = a[r][c];
                a[r][c] = iBK;
            }
        }

        public boolean slideCol(){
            boolean res = false;
            for (int c = 1; c < nC; c ++){
                if (colIsEmpty(c - 1) && !colIsEmpty(c)){
                    swapCol(c);
                    res = true;
                }
            }
            return res;
        }

        public boolean noMoreMoves(){
            for (int r = 0; r < nR; r ++){
                for (int c =0; c < nC; c ++){
                    if (infectable(r, c)){
                        return false;
                    }
                }
            }
            return true;
        }

    }
}
