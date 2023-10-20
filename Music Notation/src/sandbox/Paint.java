package sandbox;

import graphics.G;
import graphics.Window;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window {
    public Paint(){super("Paint", 1500, 750);}

    public static Pic thePic = new Pic();;
    public static Path thePath;
    public static Color c = G.rndColor();
    public static int clicks = 0;

    public void mousePressed(MouseEvent me){
        clicks ++;
        thePath = new Path();

        thePath.add(me.getPoint());
        thePic.add(thePath);

        repaint();
    }
    public void mouseDragged(MouseEvent me){
        thePath.add(me.getPoint());
        repaint();
    }


    @Override
    public void paintComponent(Graphics g){
        G.clear(g);
        /*Color c = G.rndColor();*/
        g.setColor(c);

        g.fillOval(100,100,200,300);
        g.drawLine(100,600,600,100);

        String msg = "Dude";
        int x = 400, y = 200;
        g.drawString(msg,x,y);

        g.fillOval(400,200,2,2);
        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent(), d = fm.getDescent();
        int w = fm.stringWidth(msg);
        g.drawRect(x,y-a,w,a+d);

        g.setColor(Color.black);
        g.drawString("Clicks, " + clicks, 400,400);

        thePic.draw(g);
    }
    //--------------------- Path -------------------------//
    public static class Path extends ArrayList<Point>{
        public void draw(Graphics g){
            for(int i = 1; i < size(); i ++){
                Point p = get(i - 1), n = get(i);
                g.drawLine(p.x, p.y, n.x, n.y);
            }
        }
    }
    //--------------------- Pic --------------------------//
    public static class Pic extends ArrayList<Path>{
        public void draw(Graphics g){
            for(Path p:this){
                p.draw(g);
            }
        }
    }

}
