package game;

import graphics.G;
import graphics.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.DigestException;

public class XEd extends Window {

    public static Exp e;
    public static  Exp.View view;
    public XEd(){
        super("Expression Editor", 1000, 700);
        Exp e2 = n0("foo");
        Exp e1 = n2("Multiply *").k0(e2).k1(e2);

        e = n2("Plus +").k0(n0("3")).k1(e1);
        e.toString();
        System.out.println(e);
        view = new Exp.View(null, e);
        Key.focus = view;
    }



    public static Exp n0 (String name){ return new Exp(name, 0);}
    public static Exp n1 (String name){ return new Exp(name, 1);}
    public static Exp n2 (String name){ return new Exp(name, 2);}

    public void paintComponent(Graphics g){
        g.setColor(Color.white);
        g.fillRect(0, 0, 5000,5000);
        g.setColor(Color.black);
        view.layout(g, 100, 100);
        view.showBox(g);
    }

    public void keyPressed(KeyEvent ke){
        Key.focus.KeyPressed(ke);
        repaint();
    }
    //----------------------KeyFocus----------------//
    public static class Key {
        public static Press NOONE = new Press(){
            public void KeyPressed(KeyEvent ke){}
        };
        public static Press focus = NOONE;
        public static interface Press{
            public void KeyPressed(KeyEvent ke);
        }
    }
    // --------------------- Exp ------------------//
    public static class Exp{
        public String name;
        public int nKids = 0;
        public Exp[] kids;

        private Exp(String name, int nKids){
            this.name = name;
            this.nKids = nKids;
            this.kids = nKids == 0 ? null : new Exp[nKids]; // allocate the size of the array
        }

        public Exp k0(Exp e){
            kids[0] = e; //sets the child in this expression
            return this;
        }
        public Exp k1(Exp e){
            kids[1] = e; //sets the child in this expression
            return this;
        }


        public String toString(){
            String res = "";
            for(int i = 0; i < nKids; i ++){
                res += " " + kids[i].toString();
            }
            res += name == "" ? "??" : " " + name;
            return res;
        }



        // -------------------View -------------------//
        public static class View implements Key.Press{

            public Exp exp; // this is the model element we view here
            public View dad; // this view had a dad was the parent
            public int nKids;
            public View[] kids; // these are the views of the model's children
            public int x, y, w, h; // this is the bounding box of the entire view
            public int dx, dy; // where to draw the operator name

            public View(View dad, Exp exp) {
                this.dad = dad;
                this.exp = exp;
                nKids = exp.nKids;
                this.kids = nKids == 0 ? null : new View[nKids]; // we simply copy the data from the model
                for (int i = 0; i < nKids; i++) {
                    kids[i] = new View(this, exp.kids[i]); // still we are copying the data
                }
            }

            public int hW(Graphics g){ // header's width
                return g.getFontMetrics().stringWidth(exp.name); // we read the value in the model and tells us how many space we have in our view
            }
            public int hH(Graphics g){ // header;s Height
                return g.getFontMetrics().getHeight();
            }

            public int width(Graphics g){
                if (w > -1){return w;}
                w = Math.max(hW(g), kW(g));
                return w;
            }

            public int height (Graphics g){
                if (h > -1) {
                    return h;
                }
                h = hH(g) + maxKH(g);
                return h;
            }

            public int maxKH(Graphics g) { // this is the maximum kid's height
                if (kids == null){return 0;}
                int max = 0;
                for (int i = 0; i < exp.nKids; i ++){
                    max = Math.max(max, kids[i].height(g));
                }
                return max;
            }

            public int kW(Graphics g) {
                if (kids == null){
                    return 0;
                }
                int res = 0;
                for (int i = 0; i < exp.nKids; i ++){
                    res += kids[i].width(g);
                }
                return  res;
            }

            public void nuke(){
                w = -1;
                h = -1;

                for (int i = 0; i < nKids; i ++){
                    kids[i].nuke();
                }

            }
            public void layout (Graphics g, int xX, int yY){
                nuke();
                width(g);
                height(g);
                setXY(g, xX, yY);
            }
            public void setXY(Graphics g, int xx, int yy){

                x =xx;
                y =yy;
                dx = (w - hW(g))/ 2;
                dy = g.getFontMetrics().getAscent();
                if (kids == null) {return;}
                int kx =(w - kW(g)) / exp.nKids; // how much white space should be distributed among kids;
                yy += hH(g);
                for (int i = 0; i < exp.nKids; i ++){
                    kids[i].w += kx;
                    kids[i].setXY(g, xx, yy);
                    xx += kids[i].w;
                }
            }
            public void showBox(Graphics g){
                g.setColor(Color.CYAN);
                g.drawRect(x,y,w,h);
                g.setColor(Color.black);
                rShow(g);
            }

            public void rShow(Graphics g) {
                if(Key.focus == this){
                    g.setColor(Color.RED);
                }
                g.drawString(exp.name, x + dx, y + dy);
                g.setColor(Color.black);
                if (kids != null){
                    int kY = kids[0].y;
                    g.drawLine(x, kY, x + w, kY);
                    kids[0].rShow(g);
                    for (int i = 1; i < exp.nKids; i++){
                        int kX = kids[i].x;
                        g.drawLine(kX, kY, kX, y + h);
                        kids[i].rShow(g);
                    }
                }
            }

            public void up(){
                if (dad != null) {Key.focus = dad;}
            }
            public void dn(){
                if (nKids > 0) {Key.focus = kids[0];}
            }
            public void left(){
                if (dad != null && dad.kids[0] != this){
                    for (int i = 0; i < dad.nKids; i ++){
                        Key.focus = dad.kids[i - 1];
                        return;
                    }
                }
            }
            public void right(){
                if (dad != null && dad.kids[dad.nKids - 1] != this){
                    for (int i = 0; i < dad.nKids; i ++){
                        Key.focus = dad.kids[i + 1];
                        return;
                    }
                }
            }

            @Override
            public void KeyPressed(KeyEvent ke) {
                int vk = ke.getKeyCode();
                if (vk == KeyEvent.VK_LEFT){
                    left();
                    return;
                }
                if (vk == KeyEvent.VK_RIGHT){
                    right();
                    return;
                }
                if (vk == KeyEvent.VK_UP){
                    up();
                    return;
                }
                if (vk == KeyEvent.VK_DOWN){
                    dn();
                    return;
                }
                if (vk == KeyEvent.VK_BACK_SPACE && exp.name.length() > 0){
                    exp.name = exp.name.substring(0, exp.name.length() - 1);
                    return;
                }
                char c = ke.getKeyChar();
                if (c != KeyEvent.CHAR_UNDEFINED){
                    exp.name += c;
                }
            }
        }
    }

    public static void main(String [] args){
        (PANEL = new XEd()).launch();
    }

}
