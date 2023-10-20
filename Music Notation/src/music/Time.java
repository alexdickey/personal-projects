package music;

import java.util.ArrayList;

public class Time {
    public int x;
    public Head.List heads = new Head.List();
    private Time(Sys sys, int x){
        this.x = x;
        sys.times.add(this);
    }

    public void unStemHeads(int y1, int y2){
        for (Head h : heads){
            int y = h.y();
            if (y > y1 && y < y2){
                h.unStem();
            }
        }
    }
//    public void stemHeads(Sys sys, boolean up, int y1, int y2){
//        Stem s = new Stem(sys ,up);
//        for (Head h : heads){
//            int y = h.y();
//            if (y > y1 && y < y2){
//                h.joinStem(s);
//            }
//        }
//        if (s.heads.size() == 0){
//            System.out.println("? WTF - Empty Head List");
//        } else{
//            s.setWrongSides();
//            s.heads.get(0).staff.sys.stems.addStem(s);
//        }
//    }

    //------------------------List-------------------//
    public static class List extends ArrayList<Time>{
        public Sys sys;
        public List(Sys sys){
            this.sys = sys;
        }
        public  Time getTime(int x){
            if (size() == 0){return new Time(sys, x);}
            Time t = getClosestTime(x);
            return (Math.abs(x - t.x) < UC.SnapTime)? t : new Time(sys, x);
        }

        private Time getClosestTime(int x) {

            Time res = get(0);
            int bestSoFar = Math.abs(x - res.x);
            for (Time t : this){
                int dis = Math.abs(x - t.x);
                if (dis < bestSoFar) {
                    res = t;
                    bestSoFar = dis;
                }
            }
            return res;

        }


    }
}
