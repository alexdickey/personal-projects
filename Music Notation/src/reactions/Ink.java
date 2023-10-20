package reactions;

import graphics.G;
import music.I;
import music.UC;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Ink  implements I.Show{

    public static Buffer BUFFER = new Buffer();
    public static G.VS TEMP = new G.VS(100,100,100,100);
    public static final int K = UC.normSampleSize;
    public Norm norm;
    public G.VS vs;
    public  Ink(){
        norm = new Norm();
        vs = BUFFER.bbox.getNewVS();
//        super(K);
//        G.Pl temp = BUFFER.subSample(K);
//        G.V.T.set(BUFFER.bbox, TEMP);
//        temp.transform();
//        G.V.T.set(TEMP, BUFFER.bbox.getNewVS());
//        temp.transform();
//        for(int i = 0; i < K; i++){
//            points[i].set(temp.points[i]);
//        }
        /*super(BUFFER.n);
        for(int i = 0; i < BUFFER.n; i++){
            points[i].set(BUFFER.points[i]);
        }*/
    }
    @Override
    public void show(Graphics g){
        g.setColor(Color.black);
//        draw(g);
        norm.drawAt(g,vs);
    }

    //-------------------Norm---------------------//
    public static class Norm extends G.Pl implements Serializable {
        public static final int N = UC.normSampleSize;
        public static final int MAX = UC.normCoordinateSize;
        public static final G.VS NCS = new G.VS(0,0,MAX,MAX);
        public Norm() {
            super(N);
            BUFFER.subSample(this);
            G.V.T.set(BUFFER.bbox, NCS);
            this.transform();

        }

        public int dist(Norm n){
            int res = 0;
            for(int i = 0; i < N; i++){

                int dx = points[i].x - n.points[i].x;
                int dy = points[i].y - n.points[i].y;
                res += dx * dx + dy*dy;
            }
            System.out.println();
            return res;
        }
        public void drawAt(Graphics g, G.VS vs){
            G.V.T.set(NCS, vs);
            for (int i = 1; i < N; i++) {
                g.drawLine(points[i-1].tx(), points[i-1].ty(),
                            points[i].tx(),points[i].ty());
            }
        }

        public void blend(Norm norm, int n){
            for (int i = 0; i < N; i ++){
                points[i].blend(norm.points[i], n);
            }
        }


    }
    //-------------------Buffer-------------------//
    public static class Buffer extends G.Pl implements I.Show, I.Area {

        public static final int MAX = UC.inkBufferMax;
        public int n; //Number of Points in Buffer
        public G.BBox bbox = new G.BBox();

        private Buffer() {
            super(MAX);
        }

        public void subSample(G.Pl pl){
            int k = pl.points.length;
            for(int i = 0; i < k; i ++) {
                pl.points[i].set(this.points[i * (n - 1) / (k - 1)]);
            }
        }

        public void add(int x, int y){
            if(n < MAX){
                /*increment after we set the x and y*/
                points[n++].set(x,y);
                bbox.add(x,y);
            }
        }
        public void clear(){n = 0;}

        @Override
        public void show(Graphics g) {
            this.drawN(g,n);
//            bbox.draw(g);
//            g.setColor(Color.blue);
//            this.drawNDots(g, n);
        }

        @Override
        public boolean hit(int x, int y) {
            return true;
        }

        @Override
        public void dn(int x, int y) {
            clear();
            bbox.set(x,y);
            add(x,y);
        }

        @Override
        public void drag(int x, int y) {
            add(x,y);
        }

        @Override
        public void up(int x, int y) {

        }
    }


    //-------------------List-------------------//
    public static class List extends ArrayList<Ink> implements I.Show{
        @Override
        public void show(Graphics g) {for(Ink ink:this){ink.show(g);}}


    }
}
