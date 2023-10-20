package game;

import graphics.G;
import graphics.Window;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

public class Skunk extends Window implements ActionListener {
    // rules
    // markov's method

    public static G.Button.List cmds = new G.Button.List();
    public static G.Button PASS = new G.Button(cmds, "PASS") {
        // create  a new button, but we need to override the act
        @Override
        public void act() {
            pass();
        }
    };

    public static G.Button AGAIN = new G.Button(cmds, "PLAY AGAIN ?") {
        // create  a new button, but we need to override the act
        @Override
        public void act() {
            again();
        }
    };

    public static G.Button ROLL = new G.Button(cmds, "ROLL") {
        // create  a new button, but we need to override the act
        @Override
        public void act() {
            roll();
        }
    };
    public static int M = 0, E = 0, H = 0; // My bank, Enemy Bank, My Hand;
    public static boolean myTurn = true;
    public static int D1, D2; // the dice
    public static int xM = 50, yM = 50;

    public static String aiName = "Betty";
    public static String skunkMessage;
    public static Timer timer;
    public static int iConverge;

    @Override
    public void actionPerformed(ActionEvent e){
        converge(100000);
        iConverge++;
        if (iConverge > 900){
            showStrategy = false;
            timer.stop();
        }
    }

    // initiation
    static {
        PASS.set(100, 100);
        ROLL.set(150, 100);
        AGAIN.set(500, 100);


        PASS.enabled = true;
        ROLL.enabled = true;
        AGAIN.enabled = true;
    }

    public Skunk() {
        super("Skunk", 1000, 750);
        timer = new Timer(15, this);
        timer.start();
    }

    public static void again() {
        // initialize the value we had
        M = 0;
        E = 0;
        H = 0;
        myTurn = G.rnd(2) == 0;
        AGAIN.set(-100, -100);

        PASS.set(100, 100);
        ROLL.set(150, 100);
        PASS.enabled = true;
        ROLL.enabled = true;

    }

    public static void pass() {
        if (myTurn) {
            M += H;
        } else {
            E += H;
        }
        H = 0;
        myTurn = !myTurn;
        roll();
    }

    public static void skunked() {
        H = 0;
        ROLL.enabled = false;

    }

    public static void normalHand() {
        H += D1 + D2;
        if (isGameOver()) {
            skunkMessage = playerName() + " WIN!!";
            gameOver();
        } else {
            if (!myTurn && ROLL.enabled) {
                setAIButtons();
            }
        }

    }

    public static void setAIButtons() {
        if (gotToRoll()) {
            PASS.enabled = false;
        } else {
            ROLL.enabled = false;
        }
    }

    public static boolean gotToRoll() {
        wOptimal(E, M, H);
        return !shouldPass;
    }

    public static void gameOver() {
        PASS.set(-100, -100);
        ROLL.set(-100, -100);
        AGAIN.set(500, 100);

    }

    public static boolean isGameOver() {
        return 100 <= H + ((myTurn) ? M : E);

    }

    public static void totalSkunk() {
        if (myTurn) {
            M = 0;
        } else {
            E = 0;
        }
        skunked();
    }


    public static void roll() {
        rollDice();
        analyzeDice();

    }

    public static void rollDice() {
        D1 = 1 + G.rnd(6);
        D2 = 1 + G.rnd(6);
    }

    public static void analyzeDice() {
        // add scores to players
        // check for certain rolls
        // set skunk message - "hey ";
        PASS.enabled = true;
        ROLL.enabled = true;

        if (D1 == 1 && D2 == 1) {
            skunkMessage = " Totally skunked!";
            totalSkunk();
            return;
        }
        if (D1 == 1 || D2 == 1) {
            skunkMessage = " Just a little skunked!";
            skunked();
            return;
        }
        skunkMessage = " ";
        normalHand();

    }

    public static void showRoll(Graphics g) {
        g.setColor(Color.black);
        g.drawString(playerName() + "Dice Roll:" + D1 + " , " + D2, xM, yM + 20);
    }

    public static String playerName() {
        return myTurn ? "Your" : aiName;
    }

    public static String scoreString() {

        return "Your Score: " + M + "    " + aiName + "'s Score: " + E + "     Hand Score: " + H + skunkMessage;

    }

    public static void showScore(Graphics g) {
        g.setColor(Color.black);
        g.drawString(scoreString(), xM, yM + 40);
    }


    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, 5000, 5000);
        if (!showStrategy) {
            cmds.show(g);
            showRoll(g);
            showScore(g);
        } else {
            showAll(g);
            g.drawString("Converge: " + iConverge, 200, 20);
        }
    }

    public void mousePressed(MouseEvent me) {
        int x = me.getX(), y = me.getY();
        if (cmds.clicked(x, y)) {
            repaint();
        }

    }

    // ------------------------ AI Calculation -----------------------------//

    public static double[][][] P = new double[100][100][100];   // Probability matrix.

    public static double p(int m, int e, int h) {
        if (m + h >= 100) {
            return 1.0;
        }
        if (e >= 100) {
            return 0.0;
        }
        return P[m][e][h];
    }

    public static double wPass(int m, int e, int h) {          // Probability of winning if you pass the dice.
        return 1.0 - p(e, m + h, 0);

    }

    public static double wTS(int m, int e, int h) {
        return 1.0 - p(e, 0, 0);
    }

    public static double wS(int m, int e, int h) {
        return 1.0 - p(e, m, 0);
    }

    public static double wRoll(int m, int e, int h) {

        double res = (wTS(m, e, h) / 36) + (wS(m, e, h) / 3.6);
        for (int d1 = 2; d1 < 7; d1++) {
            for (int d2 = 2; d2 < 7; d2++) {
                res += (p(m, e, h + d1 + d2) / 36);
            }
        }
        return res;
    }

    public static boolean shouldPass;

    public static double wOptimal(int m, int e, int h) {
        double wP = wPass(m, e, h);
        double wR = wRoll(m, e, h);
        return (shouldPass = (wP > wR)) ? wP : wR;
    }

    public static void converge(int n) {
        for (int i = 0; i < n; i++) {
            int m = G.rnd(100), e = G.rnd(100), h = G.rnd(100 - m);
            P[m][e][h] = wOptimal(m, e, h);
        }
    }

    public static final int W = 7;                  // pixel count cell width
    public static boolean showStrategy = false;      // show strategy matrix

    public static void showAll(Graphics g) {
        showStops(g);
        showGrid(g);
        showColorMap(g);
    }

    private static void showStops(Graphics g) {
        for (int m = 0; m < 100; m++) {
            for (int e = 0; e < 100; e++) {
                int k = firstStop(m, e);
                g.setColor(stopColor[k]);
                g.fillRect(xM + W * m, yM + W * e, W, W);
            }
        }
    }

    private static int firstStop(int m, int e) {
//        int h = 0;
        for (int h = 0; h < 100 - m; h++) {
            wOptimal(m, e, h);
            if (shouldPass) {
                return (h >= nC) ? 0 : h;
            }
        }
        return 0;
    }

    private static void showGrid(Graphics g) {
        g.setColor(Color.BLACK);
        for (int k = 0; k <= 10; k++) {
            int d = 10 * W * k;
            g.drawLine(xM, yM + d, xM + 100 * W, yM + d);
            g.drawLine(xM + d, yM, xM + d, yM + 100 * W);
        }
    }

    private static void showColorMap(Graphics g) {
        int x = xM + 100 * W + 30;
        for (int i = 0; i < nC; i++) {
            g.setColor(stopColor[i]);
            g.fillRect(x, yM + 15 * i, 15, 13);
            g.setColor(Color.BLACK);
            g.drawString("" + i, x + 20, yM + 15 * i + 10);
        }
    }

    public static final int nC = 45;     // Number of colors.
    public static Color[] stopColor = new Color[nC];

    static {
        for (int i = 0; i < nC; i++) {
            stopColor[i] = G.rndColor();
        }
    }

    public static void main(String[] args) {
        (PANEL = new Skunk()).launch();
    }

}