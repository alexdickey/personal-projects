package music;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{

    public Staff staff;
    public int line;
    public Time time;

    public Glyph forcedGlyph = null; // placeholder for the future glyph
    public  Stem stem = null;
    public  boolean wrongSide = false;

    public Head(Staff staff, int x, int y) {
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        time.heads.add(this);
        int H = staff.fmt.H;
//        int top = staff.yTop() - H;
//        line = ((y - top + H/2) / H) - 1;
        this.line = staff.lineOfY(y);
//        System.out.println("Line: " + line);

        addReaction(new Reaction("S-S") {

            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                int w = Head.this.w(), hy = Head.this.y();
                if (y1 > y || y2 < y){
                    return  UC.noBid;
                }
                int hLeft = Head.this.time.x, hRight = hLeft + w;
                if (x < hLeft - w || x > hRight + w){
                    return UC.noBid;
                }
                if (x < hLeft + w/2){
                    return hLeft - x;
                }
                if (x > hRight - w/2){
                    return x - hRight;
                }
                return UC.noBid;
            }


            public void act(Gesture gesture) {
                int x = gesture.vs.xL(), y1 = gesture.vs.yL(), y2 = gesture.vs.yH();
                Time t = time;
                int w = Head.this.w();
                boolean up = x > (t.x + w/2);

                if (Head.this.stem == null){
//                    t.stemHeads(Head.this.staff.sys ,up, y1, y2);
                    Stem.getStem(t, y1, y2, up);
                }else{
                    t.unStemHeads(y1, y2);

                }
            }
        });

        addReaction(new Reaction("DOT") {

            public int bid(Gesture gesture) {
                if (Head.this.stem == null){
                    return  UC.noBid;
                }
                int xh = Head.this.x(), yh = Head.this.y(), h = Head.this.staff.fmt.H, w = Head.this.w();
                int x = gesture.vs.xM(), y = gesture.vs.yM();
                if (x < xh || x > xh + 2*w || y < yh - h || y > yh + h ){
                    return  UC.noBid;
                }
                return (Math.abs(xh + w - x)) + Math.abs(yh - y);
            }


            public void act(Gesture gesture) {
                Head.this.stem.cycleDot();
            }
        });
    }

    public int w(){
        return 24 * staff.fmt.H/10;
    }

    public void show(Graphics g){
        int H = staff.fmt.H;
        g.setColor(wrongSide ? Color.red: Color.black);
        (forcedGlyph != null ? forcedGlyph : normalGlyph()).showAt(g, H, x(), y());
        if (stem != null){
            int off = UC.augDotOffset, sp = UC.augDotSpace;
            for (int i = 0; i < stem.nDot; i ++){
                g.fillOval(time.x + off + i * sp, y() - 3 * H/2, H * 2 / 3, H * 2 / 3);
            }
        }
    }

    public int x(){
        int res =  time.x;
        if (wrongSide){
            res += (stem != null && stem.isUp) ? w() : -w();
        }
        return  res;
    }

    public int y(){
        return staff.yLine(line);
    }

    public Glyph normalGlyph(){
        if (stem == null){
            return  Glyph.HEAD_Q;
        }
        if (stem.nFlag == -1){
            return Glyph.HEAD_HALF;
        }
        if (stem.nFlag == -2){
            return Glyph.HEAD_W;
        }
        return  Glyph.HEAD_Q;
    }

    public void deleteHead(){

        time.heads.remove(this);
    }
    public void unStem(){
        if (stem != null){
            stem.heads.remove(this);
            if (stem.heads.size() == 0){
                stem.deleteStem();
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        // stub
        if (stem != null){
            unStem();
        }
        s.heads.add(this);
        stem = s;
    }

    @Override
    public int compareTo(Head h) {
        return (staff.iStaff != h.staff.iStaff) ? staff.iStaff - h.staff.iStaff : this.line - h.line;
    }


    // ----------------------------List-------------------------------//
    public static class List extends ArrayList<Head>{

    }
}
