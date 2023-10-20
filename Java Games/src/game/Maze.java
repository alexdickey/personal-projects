package game;

import graphics.G;
import graphics.Window;
import java.awt.Color;
import java.awt.Graphics;

public class Maze extends Window {
  public static final int W = 90, H = 60, xM = 50, yM = 50, C = 10;  // C: cell width
  public static int[] n = new int[W + 1], p = new int[W + 1];  // last index is W in these arrays, n: next, p: previous
  public static int Y = yM;  // set y to y Margin
  public static Graphics gg;

  public Maze() {
    super("Maze", 1000, 700);
  }
  public static int x(int i) {
    return xM + i * C;
  }
  public static void hLine(int i) {gg.drawLine(x(i), Y, x(i + 1), Y);}
  public static void vLine(int i) {gg.drawLine(x(i), Y, x(i), Y + C);}
  public static void hRowZero() {Y = yM; atomize(); for(int i = 0; i < W; i++) {
    hLine(i);
    join(i, i + 1);
  }}

  public static void vRule(int i) {
    if(i == 0 || i == W || isAtom(i) || pV()) {
      vLine(i);
    } else {cut(i);}
  }

  public static void hRule(int i) {
    if(!same(i, i + 1) && pH()) {
      hLine(i);
      join(i, i + 1);
    }
  }
  public static void vLast() {
    vLine(0);
    vLine(W);
    for(int i = 0; i < W; i++) {
      if (!same(i, 0)) {join(i, 0); vLine(i);}
    }
  }
  public static void hLast() {
    Y += C;
    for(int i = 0; i < W; i++) {
      hLine(i);
    }
  }
  public static boolean pH(){
    return G.rnd(100) < 47;  // will return a %
  }

  public static boolean pV(){
    return G.rnd(100) < 33;  // will return a % that is connected
  }
  public static void hRow() {for(int i = 0; i < W; i++) {hRule(i);}}
  public static void vRow() {for(int i = 0; i <= W; i++) {vRule(i);}}

  public void paintComponent(Graphics g) {
    gg = g;
    G.clear(g);
    g.setColor(Color.BLACK);
    hRowZero();
    for(int i = 0; i < H; i++) {
      vRow();
      Y += C;
      hRow();
    }
    vLast();
    hLast();
  }

  // -------------- Circular List -------------------//
  public static void atomize(int i) {n[i] = i; p[i] = i;}
  public static void atomize() {for(int i = 0; i <= W; i++) { atomize(i); }}
  public static void next(int a, int b) {n[a] = b; p[b] = a;}
  public static void join(int a, int b) {int nA = n[a], pB = p[b];next(a, b); next(pB, nA);}
  public static void cut(int a) {join(p[a], n[a]);}
  public static boolean isAtom(int a) {return n[a] == a;}  // determined if the number is singleton
  public static boolean same(int a, int b) {
    if(a == b) {return true;}
    int start = a;
    a = n[a];
    while(a != start) {
      if(a == b) {return true;}
      a = n[a];
    }
    return false;
  }

  // ------------- Test --------------- //
  public static String asString(int a) {
    int start = a;
    String res = "(" + a;
    String sep = ", ";
    a = n[a];
    while(a != start) {
      res += sep + a;
      a = n[a];
    }
    return res + ")";
  }

  public static String showAll() {
    String res = "";
    for(int i = 0; i <= W; i++) {
      res += asString(i);
    }
    return res;
  }
  public static void test() {
//    atomize();
//    System.out.println(showAll());
//
//    join(1, 3);
//    System.out.println("after join 1, 3 " + showAll());
//
//    join(2, 3);
//    System.out.println("after join 2, 3 " + showAll());
//
//    cut(1);
//    System.out.println("after cut 1 " + showAll());
//
//    cut(1);
//    System.out.println("after cut 1 " + showAll());
//
//    System.out.println("is Atom 1 " + isAtom(1));
//    System.out.println("is Atom 2 " + isAtom(2));
//
//    System.out.println("is same 1, 2 " + same(1, 2));
//    System.out.println("is same 2, 3 " + same(2, 3));
  }


  public static void main(String[] args) {
    (PANEL = new Maze()).launch();
    test();
  }
}
