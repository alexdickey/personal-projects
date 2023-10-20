package music;

import java.awt.Graphics;
import java.util.ArrayList;

import reactions.Gesture;
import reactions.Mass;
import reactions.Reaction;

import static sandbox.Music.PAGE;

public class Clef extends Mass{

    public static int INITIALX = UC.defaultPageMargin + UC.initialClefOffset;

    public Glyph glyph;
    public Staff staff;
    public int x;

    public Clef(Glyph glyph, Staff staff, int x) {
        super("BACK");
        this.glyph = glyph;
        this.staff = staff;
        this.x = x;

        addReaction(new Reaction("S-S") { // add F Clef
            @Override
            public int bid(Gesture gesture) {
                int x = gesture.vs.xL(), y = gesture.vs.yL();
                int dX = Math.abs(x - Clef.this.x), dY = Math.abs(y - Clef.this.staff.yLine(4));
                if(dX > 30 || dY > 30) {return UC.noBid;}
                return 10;
            }

            @Override
            public void act(Gesture gesture) {
                Clef.this.deleteClef();
            }
        });

    }

    public static void setInitialClefs(Staff staff, Glyph glyph) {
        ArrayList<Sys> systems = PAGE.sysList;
        Sys firstSys = staff.sys;
        int iStaff = staff.iStaff;
        for(int i = firstSys.iSys; i < systems.size(); i++) {
            systems.get(i).staffs.get(iStaff).initialClef.glyph = glyph;
        }
    }

    public void show(Graphics g) {
        if(glyph != null) {glyph.showAt(g,  staff.fmt.H, x, staff.yLine(4));}
    }

    public void deleteClef() {
        if(x == INITIALX) {
            glyph = null;
        } else {
            deleteMass();
        }
    }

}
