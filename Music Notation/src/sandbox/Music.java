package sandbox;

import graphics.G;
import graphics.Window;
import music.*;
import reactions.Gesture;
import reactions.Ink;
import reactions.Layer;
import reactions.Reaction;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Music extends Window {
    static{
        new Layer("BACK");
        new Layer("NOTE");
        new Layer("FORE");
    }
    public static Page PAGE;

    static int[] xPoly = {100, 200, 200, 100};
    static int[] yPoly = {50, 70, 80, 60};
    static Polygon poly = new Polygon(xPoly, yPoly, 4);
    public static void main(String[] args){
        (PANEL = new Music()).launch();
    }
    public Music() {

        super("Music Editor", UC.initialWindowWidth, UC.initialWindowHeight);
        Reaction.initialReactions.addReaction(new Reaction("E-E") {
            public int bid(Gesture gesture) {
                return 10;
            }

            public void act(Gesture gesture) {
                int y = gesture.vs.yM(); // yM stands for y middle
                Sys.Fmt sysFmt = new Sys.Fmt();
                PAGE = new Page(sysFmt);
                PAGE.margins.top = y;
                PAGE.addNewSys();
                PAGE.addNewStaff(0);
                this.disable();
            }
        });
    }


    public void paintComponent(Graphics g){
        G.clear(g);
        g.setColor(Color.blue);
        g.drawString("Music", 100, 30);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
//        if (PAGE != null ){
//            Glyph.CLEF_G.showAt(g, 8, 100, PAGE.margins.top + 4 * 8);
//            Glyph.HEAD_HALF.showAt(g, 8, 200, PAGE.margins.top + 4 * 8);
//            Glyph.HEAD_Q.showAt(g, 8, 300, PAGE.margins.top + 4 * 8);
//            Glyph.HEAD_W.showAt(g, 8, 400, PAGE.margins.top + 4 * 8);
//            Glyph.REST_1F.showAt(g, 8, 500, PAGE.margins.top + 4 * 8);
//            Glyph.REST_2F.showAt(g, 8, 600, PAGE.margins.top + 4 * 8);
//            Glyph.REST_3F.showAt(g, 8, 700, PAGE.margins.top + 4 * 8);
//            Glyph.REST_4F.showAt(g, 8, 800, PAGE.margins.top + 4 * 8);
//        }
//        int h = 8, x1 = 100, x2 = 200;
//        Beam.setMasterBeam(x1, 100 + G.rnd(100), x2, 100 + G.rnd(100));
//        Beam.drawBeanStack(g, 0, 1, x1, x2, h);
//        g.setColor(Color.orange);
//        Beam.drawBeanStack(g, 1, 3, x1 + 10, x2 - 10, h);


    }
    public void mousePressed(MouseEvent me){Gesture.AREA.dn(me.getX(), me.getY()); repaint();}
    public void mouseDragged(MouseEvent me){Gesture.AREA.drag(me.getX(), me.getY()); repaint();}
    public void mouseReleased(MouseEvent me){Gesture.AREA.up(me.getX(), me.getY()); repaint();}

}
