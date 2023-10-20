package music;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import java.awt.*;
import java.util.ArrayList;

import static sandbox.Music.PAGE;

public class Page extends Mass {
    public Margins margins = new Margins();
    public Sys.Fmt sysFmt;
    public int sysGap;
    public ArrayList<Sys> sysList = new ArrayList<>();

    public Page(Sys.Fmt sysFmt) {
        super("BACK");
        this.sysFmt = sysFmt;
        addReaction(new Reaction("E-W") { // add a new staff to sys format

            public int bid(Gesture gesture) {
                int y = gesture.vs.yM();
                if (y <= PAGE.margins.top + sysFmt.height() + 30) {
                    return  UC.noBid;
                }
                return 50;
            }


            public void act(Gesture gesture) {
                int y = gesture.vs.yM();
                PAGE.addNewStaff(y - PAGE.margins.top);
            }
        });

        addReaction(new Reaction("E-E") { // add a new sys

            public int bid(Gesture gesture) {
                int y = gesture.vs.yM();
                int yBot = PAGE.sysTop(PAGE.sysList.size() - 1) + sysFmt.height() + 20;
                if (y < yBot){
                    return UC.noBid;
                }
                return 50;
            }

            public void act(Gesture gesture) {
                int y = gesture.vs.yM();
                if (PAGE.sysList.size() == 1){
                    PAGE.sysGap = y - PAGE.margins.top -sysFmt.height();
                }
                PAGE.addNewSys();
            }
        });

    }
    public void addNewSys(){
        sysList.add(new Sys(this, sysList.size(), sysFmt));
    }

    public void addNewStaff(int yOffset){
        Staff.Fmt fmt = new Staff.Fmt();
        int n = sysFmt.size();
        sysFmt.add(fmt);
        sysFmt.staffOffset.add(yOffset);
        for (int i =0; i< sysList.size(); i ++){
            Sys sys = sysList.get(i);
            sysList.get(i).staffs.add(new Staff(sys, n, fmt));
        }
    }
    public int sysTop(int iSys) {
        return margins.top + iSys * (sysFmt.height() + sysGap);
    }
    public void show(Graphics g){
        for (int i = 0; i < sysList.size(); i ++){
            sysFmt.showAt(g, sysTop(i));
        }
    }

    //-----------------------------Margins----------------------//
    public static class Margins {
        private static int M = 50;
        public int top  = M, left = M, by = UC.initialWindowHeight - M, right = UC.initialWindowWidth - M;
    }
}
