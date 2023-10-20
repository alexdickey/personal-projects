package graphics;

import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
import javax.swing.plaf.ViewportUI;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class G {

    public static G.V LEFT = new G.V(-1, 0), RIGHT = new G.V(1, 0);
    public static G.V UP = new G.V(0, -1), DOWN = new G.V(0, 1);
    public static Random RND = new Random();
    public static int rnd (int max){ return RND.nextInt(max);}
    public static Color rndColor(){
        return new Color(rnd(256),rnd(256),rnd(256));
    }
    public static void clear(Graphics g){
        g.setColor(Color.white); g.fillRect(0,0,5000,5000);
    }


    public static void drawCycle(Graphics g, int x, int y , int r){
        g.drawOval(x - r,y - r,2*r, 2*r);
    }

    //-----------------------V (Vector)-------------------------//
    public  static class V implements  Serializable{

        public static Transform T = new Transform();
        public int x, y;

        public V(int x, int y){this.set(x,y);}

        public V() {

        }

        public void set(int x, int y){this.x = x;this.y = y;}

        public void set(V v){this.x = v.x; this.y = v.y;};
        public void add(V v){ x += v.x; y += v.y;}
        public void setT(V v){
            set(v.tx(),v.ty());
        }
        public int tx(){return x * T.n / T.d + T.dx;}

        /* oW is the old width, nW is the new width */
        public int ty(){
            return y * T.n / T.d + T.dy;
        }

        public void blend (V v, int k){
            set((k * x + v.x)/ (k+1), (k*y + v.y)/(k+1));
        }

        //-------------------------TransForm----------------------//
        public static class Transform{
            /* n is numerator the d is a denominator */
            /* x1 = x * (n /d) + dx */
            public int dx, dy,n,d;

            public void setScale(int oW,int oH, int nW,int nH){
                /* Math.max(nW,nH) */
                n = (nW > nH)? nW : nH;
                d = (oW > oH)? oW : oH;
            }

            public int offSet(int oX, int oW, int nX, int nW){
                return (-oX - oW/2)*n/d + nX + nW/2;
            }
            public void set(VS oVS, VS nVS){
                setScale(oVS.size.x, oVS.size.y,nVS.size.x,nVS.size.y);
                dx = offSet(oVS.loc.x, oVS.size.x,nVS.loc.x,nVS.size.x);
                dy = offSet(oVS.loc.y, oVS.size.y,nVS.loc.y,nVS.size.y);
            }

            public void set(BBox  oB, VS nVS){
                setScale(oB.h.size(), oB.v.size(),nVS.size.x,nVS.size.y);
                dx = offSet(oB.h.lo, oB.h.size(),nVS.loc.x,nVS.size.x);
                dy = offSet(oB.v.lo, oB.v.size(),nVS.loc.y,nVS.size.y);
            }




        }


    }


    //-----------------------VS---------------------------------//
    public  static class VS {
        public V loc,size;
        public VS (int x, int y, int w, int h){
            loc = new V(x, y);
            size = new V(w,h);
        }
        public void fill(Graphics g, Color c){
            g.setColor(c);
            g.fillRect(loc.x,loc.y,size.x,size.y);
        }

        public void draw(Graphics g, Color c){
            g.setColor(c);
            g.drawRect(loc.x,loc.y,size.x,size.y);
        }

        public boolean hit(int x, int y){
            /* && is a shortcut and */
            return loc.x < x && loc.y < y && x < (loc.x + size.x) && y < (loc.y +size.y);
        }

        public int xL() {return  loc.x;};
        public int xM() {return  loc.x + size.x / 2;}
        public int xH() {return  loc.x + size.x;};

        public int yL() {return  loc.y;};
        public int yM() {return  loc.y + size.y / 2;}
        public int yH() {return  loc.y + size.y;};
        public void resize(int x, int y){
            if(x > loc.x && y > loc.y){
                size.set(x - loc.x, y - loc.y);
            }
        }



    }

    //-----------------------LowHigh----------------------------//
    public  static class LoHi {
        public int lo, hi;
        public LoHi(int min, int max){
            lo = min;
            hi = max;
        }
        public void add(int x){
            if (x < lo){ lo = x;}
            if (x > hi){ hi = x;}
        }
        public  void set (int x){
            lo = x;
            hi = x;
        }
        public int size(){return (hi - lo) == 0 ? 1 : hi - lo;}

    }

    //-----------------------BBox(Banding box)------------------------------//
    public  static class BBox{
        // horizontal and vertical
        public LoHi h,v;
        public BBox(){
            h = new LoHi(0,0);
            v = new LoHi(0,0);
        }
        public void set(int x, int y){
            h.set(x);
            v.set(y);
        }
        public void add (V v){
            h.add(v.x);
            this.v.add(v.y);
        }
        public void add(int x, int y){
            h.add(x);
            v.add(y);
        }

        public VS getNewVS(){
            return new VS(h.lo,v.lo,h.size(),v.size());
        }
        public void draw(Graphics g){
            g.drawRect(h.lo,v.lo,h.size(),v.size());
        }
    }

    //----------------------- Pl Polyline-------------------------------//
    public  static class Pl implements Serializable {
        public V [] points;
        public Pl(int count){
            points = new V[count];
            for(int i =0; i < count; i ++){
                points[i] = new V(0,0);
            }
        }

        public int size (){
            return points.length;
        }
        public void transform(){
            for(int i = 0; i < points.length; i++){
                points[i].setT(points[i]);
            }
        }
        public void drawN(Graphics g, int n){
               for(int i =1; i<n; i++) {
                   g.drawLine(points[i - 1].x, points[i - 1].y, points[i].x, points[i].y);
               }
        }

        public void drawNDots(Graphics g, int n){
            for (int i = 0; i < n; i ++){
                drawCycle(g,points[i].x,points[i].y, 2);
            }
        }

        public void draw (Graphics g){
            drawN(g,size());
        }
    }

    //-----------------------Button----------------------//

    // responsible for drawing a button on the screen, detecting the mouse, visual feedback,
    // doesn't have any idea what is supposed to happen when you press the button
    public static abstract class Button{

        public abstract void act(); // the word abstract is not going to
        public boolean enabled = true, bordered = true;
        public String text = "";
        public VS vs = new VS(0,0,0,0);
        public LookAndFeel lnf = new LookAndFeel();
        public Button(Button.List list, String str) {
            if (list != null){list.add(this);}
            text = str;
        }

        // what should a button do
        public void show(Graphics g){
            if (vs.size.x == 0){setSize(g);} // text in the button demands the resizing
            vs.fill(g, lnf.back);
            // test the border
            if (bordered){vs.draw(g,lnf.border);}

            g.setColor((enabled) ? lnf.enable : lnf.disable);
            g.drawString(text, vs.loc.x + lnf.m.x, vs.loc.y + lnf.dyText);
        }
        public void setSize(Graphics g){
            FontMetrics fm = g.getFontMetrics(); // fm object which fetches the height (and other metrics) of the FONT.
            vs.size.set(2 * lnf.m.x + fm.stringWidth(text), 2 * lnf.m.y + fm.getHeight());
            lnf.dyText = fm.getAscent() + lnf.m.y; // how far the text of from the baseline
        }

        public void set(int x, int y){ // set the location of the box
            vs.loc.set(x, y);
        }

        public boolean hit(int x, int y){return vs.hit(x, y);}

        public void click(){if (enabled){act();}}



        // ------------------------- LOOK AND FEEL-----------------//
        public static class LookAndFeel{
            public Color back = Color.white;
            public Color border =Color.pink;
            public Color enable = Color.BLACK;
            public Color disable = Color.GRAY;
            public V m = new V(5, 3);
            public int dyText = 0;
        }

        //------------------------------List-------------------------//
        public static class List extends ArrayList<Button>{
            public Button hit (int x, int y){
                for (Button b : this){
                    if (b.hit(x,y)){
                        return b;
                    }
                }
                return null;
            }
            // either no button is hit or we found the button and clicked that button;
            public boolean clicked(int x, int y){
                // return true if button was clicked
                Button b = hit(x, y);
                if (b == null) return false;
                b.click();
                return true;
            }

            public void show(Graphics g) {
                for (Button b : this){b.show(g);}

            }
        }

    }





}
