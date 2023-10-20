package sandbox;

import graphics.G;
import graphics.Window;
import music.UC;
import reactions.Ink;
import reactions.Shape;
import reactions.Shape.Prototype;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PaintInk extends Window {
    public static String recognized = "";

    public PaintInk() {super("PaintInk", UC.initialWindowWidth, UC.initialWindowHeight);}

    public static Ink.List inkList = new Ink.List();
    public static Prototype.List pList = new Prototype.List();
    /*static {
        inkList.add(new Ink());
    }*/

    @Override
    public void paintComponent (Graphics g){
        G.clear(g);

        inkList.show(g);

        g.setColor(Color.red);

        Ink.BUFFER.show(g);

        if(inkList.size() > 1){
            int last = inkList.size() - 1;
            int dist = inkList.get(last).norm.dist(inkList.get(last - 1).norm);
            g.setColor((dist < UC.noMatchDist ? Color.green: Color.red));
            g.drawString("dist: " +  dist, 600,60);
        }
        g.drawString("points: "+ Ink.BUFFER.n, 600 ,30);
        pList.show(g);
        g.drawString(recognized, 700, 40);
    }

    public void mousePressed(MouseEvent me){
        Ink.BUFFER.dn(me.getX(),me.getY());
        repaint();
    }

    public void mouseDragged (MouseEvent me){
        Ink.BUFFER.drag(me.getX(),me.getY());
        repaint();
    }

    public void mouseReleased (MouseEvent me){
        Ink ink = new Ink();
        Shape s = Shape.recognize(ink);
        recognized = "recognized: " + ((s != null)? s.name : "Unrecognized");
        inkList.add(ink);
        Prototype proto;
        if (pList.bestDist(ink.norm) < UC.noMatchDist){
            proto = Prototype.List.bestMatch;

            proto.blend(ink.norm);
        }else{
            proto = new Prototype();
            pList.add(proto);
        }
        ink.norm = proto;
//        inkList.add(new Ink());
        repaint();
    }

}
