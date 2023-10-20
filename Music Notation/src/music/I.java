package music;

import graphics.G;
import reactions.Gesture;

import java.awt.*;


/*Interface is nothing but abstract function*/
public interface I {


    public interface Show {public void show(Graphics g);}
    public interface ACT {public void act(Gesture gesture);}
    public interface React extends ACT {public int bid(Gesture gesture);}

    public interface Draw{public void draw(Graphics g);/*this is a signature  A abstract function*/}
    public interface Hit{public boolean hit(int x, int y);}

    public interface Area extends Hit{
        public void dn(int x, int y);
        public void drag(int x, int y);
        public void up(int x,int y);

    }


}
