package reactions;

import music.I;
import music.UC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


// the Reaction is a abstract class everything inside the class have
// to have the structure as React.
public abstract class Reaction implements I.React {
    public Shape shape;
    // byShape store the list of shape with reactions just like the Mass class
    private static Map byShape = new Map();
    public static List initialReactions = new List();
    public Reaction (String shapeName){
        shape = Shape.DB.get(shapeName);
        if(shape == null){
            System.out.println("wtf-shapeDB doesn't know " + shapeName);
        }
    }
    public void enable(){
        // we insert the list in the byShape list
        List list = byShape.getList(shape);
        if(! list.contains(this)){
            list.add(this);
        }
    }
    public void disable(){
        List list = byShape.getList(shape);
        list.remove(this);
    }
    public static Reaction best(Gesture g){
        return byShape.getList(g.shape).loBid(g);
    }
    public static void nuke(){ //resetting for undo
        byShape = new Map();
        initialReactions.enable();
    }
    // --------------------------------// List //----------------------------//
    public static class List extends ArrayList<Reaction>{
        // this addReaction can have the parent's feature like add funtion
        // but we can adding new function inside the addReaction funtion.
        public void addReaction(Reaction r){
            // add the reaction to the array list
            add(r);
            r.enable();
        }
        public void enable() {
            for(Reaction r : this){
                r.enable();
            }
        }
        public void  removerReaction(Reaction r){
            remove(r);
            r.disable();
        }
        public void clearAll(){
            for (Reaction r : this){
                // disable is to remove the list inside the byshape map (that's the "disable"'s target)
                // it still not cleared in the list.
                r.disable();
            }
            // clear is to clear all the reaction in the list.
            this.clear();
        }
        public Reaction loBid(Gesture g){ // can return null  List could be empty, or no one bib on that stroke
            Reaction res = null;
            int bestSoFar = UC.noBid;
            for (Reaction r : this){
                int b = r.bid(g);
                if (b < bestSoFar){
                    bestSoFar = b;
                    res = r;
                }
            }
            return res;
        }

    }
    //--------------------------------Map-----------------------------------//
    public static class Map extends HashMap<Shape, List>{
        public List getList(Shape s){ // Always succeeds
            List res = get(s);
            if (res == null){
                res = new List();
                put(s, res);
            }
            return res;
        }
    }

}







