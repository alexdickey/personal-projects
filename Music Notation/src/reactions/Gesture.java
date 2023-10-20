package reactions;

import graphics.G;
import music.I;

import java.util.ArrayList;

public class Gesture {
    //
    public static List UNDO = new List();
    public static I.Area AREA = new I.Area(){
        public boolean hit(int x, int y){return true;}
        public void dn(int x, int y){ Ink.BUFFER.dn(x, y);}
        public void drag(int x, int y){ Ink.BUFFER.drag(x,y);}

        public void up(int x, int y){
            Ink.BUFFER.add(x, y);
            Ink ink = new Ink();
            Gesture gest = Gesture.getNew(ink); // Can fail if recognized.
            Ink.BUFFER.clear();
            if (gest != null){
                System.out.println("Saw: " + gest.shape.name);
                if (gest.shape.name.equals("N-N")){
                    undo();
                }
                else{
                    gest.doGesture();
                }
//                Reaction r = Reaction.best(gest); // Can fail
//                if (r != null){r.act(gest);*/

            }
        }
    };
    public Shape shape;
    public G.VS vs;
    // factory pattern.
    private Gesture(Shape shape, G.VS vs){
        this.shape = shape;
        this.vs = vs;
    }
    public static Gesture getNew(Ink ink){ // this can return null
        Shape s = Shape.recognize(ink);
        return (s == null) ? null : new Gesture(s, ink.vs);
    }
    private void redoGesture(){
        Reaction r = Reaction.best(this);
        if (r != null) {
            r.act(this);
        }
    }
    private void doGesture(){
        Reaction r = Reaction.best(this);
        if (r != null) {
            UNDO.add(this);
            r.act(this);
        }
    }

    public static void undo(){
        if (UNDO.size() > 0){
            UNDO.remove(UNDO.size() -  1);
            Layer.nuke(); // data base by shape
            Reaction.nuke();
            UNDO.redo();
        }
    }
    // --------------------------------List-------------------------//
    public static class List extends ArrayList<Gesture>{

        private void redo(){for (Gesture g : this){g.redoGesture();}}

    }

}
