package music;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

import static sandbox.Music.PAGE;

public class Sys extends Mass {
    public ArrayList<Staff> staffs = new ArrayList<>();
    public Page page;
    public int iSys;
    public Sys.Fmt fmt;
    public Stem.List stems = new Stem.List();

    public Time.List times;
    public Sys(Page page, int iSys, Sys.Fmt fmt) {
        super("BACK");
        this.page = page;
        this.iSys = iSys;
        this.fmt = fmt;
        this.times = new Time.List(this);

        for (int i = 0; i < fmt.size(); i ++){
            addStaff(new Staff(this, i, fmt.get(i)));
        }
        addReaction(new Reaction("E-E") { // add beam;

            public int bid(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL(), x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                if (Sys.this.stems.fastReject(y1, y2)){
                    return  UC.noBid;
                }
                ArrayList<Stem> temp = Sys.this.stems.allIntersectors(x1, y1, x2, y2);
                if (temp.size() < 2){
                    return  UC.noBid;
                }
                System.out.println("Crossed " + temp.size() + " stems");
                Beam beam = temp.get(0).beam;
                for (Stem s : temp){
                    if (s.beam != beam){
                        return  UC.noBid;
                    }
                }
                System.out.println("ALl stem shares beams");
                if (beam == null & temp.size() != 2){
                    return  UC.noBid;
                }
                if (beam == null &&(temp.get(0).nFlag != 0 || temp.get(1).nFlag != 0)){
                    return  UC.noBid;
                }
                return 50;
            }


            public void act(Gesture gesture) {
                int x1 = gesture.vs.xL(), y1 = gesture.vs.yL(), x2 = gesture.vs.xH(), y2 = gesture.vs.yH();
                ArrayList<Stem> temp = Sys.this.stems.allIntersectors(x1, y1, x2, y2);
                Beam beam = temp.get(0).beam;
                if (beam == null) {
                    new Beam(temp.get(0), temp.get(1));
                }else{
                    for (Stem s : temp){
                        s.incFlag();
                    }
                }

            }
        });
    }

    public void addStaff(Staff s){
        staffs.add(s);
    }

    public int  yTop(){
        return page.sysTop(iSys);
    }

    public int yBot(){
        return staffs.get(staffs.size() - 1).yBot();
    }

    public Time getTime(int x){
        return times.getTime(x);
    }

    public void show(Graphics g){
        int y = yTop(), x = PAGE.margins.left;
        g.drawLine(x, y, x, y + fmt.height());
    }

    //--------------------------Fmt-------------------//
    public static class Fmt extends ArrayList<Staff.Fmt>{
        public ArrayList<Integer> staffOffset = new ArrayList<>();

        public int height() {
            int last = size() - 1;
            return staffOffset.get(last) + get(last).height();
        }
        public void showAt(Graphics g, int y){
            for(int i =0; i < size() ; i++){
                get(i).showAt(g, y + staffOffset.get(i));
            }
        }
    }

}
