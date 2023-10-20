package game;

import graphics.G;
import graphics.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BreakOut extends Window implements ActionListener {

    public static final int H = 16, GAP = 3 * H, W = 60, PW = 70,
                            N_BRICK = 13,
                            LEFT = 100, RIGHT = LEFT + N_BRICK * W,
                            TOP = 50, BOT = 700;


    public static Paddle paddle = new Paddle();
    public static Ball ball = new Ball();

    public static final int MAX_LIFE = 3;
    public static int life = MAX_LIFE, score = 0;
    public static int row_count = 4;
    public static Timer timer;

    public BreakOut() {
        super("BreakOut", 1000, 800);
        timer = new Timer(30, this);
        timer.start();
        startGame();
    }

    public void startGame(){
        life = 3;
        score = 0;
        row_count = 0;
        startNewRows();

    }

    public static void startNewRows() {
        row_count ++;
        Brick.ALL.clear();
        Brick.newBrickRows(row_count);
        ball.init();
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        g.setColor(Color.black);
        g.fillRect(LEFT, TOP, RIGHT - LEFT, BOT - TOP);
        paddle.show(g);
        showText(g);
        ball.show(g);
        Brick.ALL.show(g);
    }

    public void showText(Graphics g) {
        g.setColor(Color.black);
        g.drawString("Lives: " + life, LEFT + 20, 30);
        g.drawString("Score: " + score, RIGHT - 80, 30);
    }

    @Override
    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if (vk == KeyEvent.VK_LEFT){paddle.left();}
        if (vk == KeyEvent.VK_RIGHT){paddle.right();}

        if (ke.getKeyChar() == ' '){unstick();}
        repaint();
    }

    public void unstick() {
        ball.init();
        paddle.dxS = -1;
    }


    public static void main(String[] args){
        (PANEL = new BreakOut()).launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ball.move();
        repaint();
    }

    //-------------------------Paddle--------------------//
    public static class Paddle extends G.VS{
        public static  final int DX = 8;

        public Color color = Color.yellow;
        public int dxS = 10;

        public Paddle() {
            super(LEFT, BOT - H, PW, H);
        }
        public void left(){
            loc.x += -DX;
            limitX();
        }
        public void right(){
            loc.x += DX;
            limitX();
        }
        public void limitX(){
            if (dxS >= 0){ball.loc.set(loc.x + dxS, BOT - 2 * H);}
            if (loc.x < LEFT){
                loc.x = LEFT;
            }
            if (loc.x > RIGHT - PW){
                loc.x = RIGHT - PW;
            }
        }

        public void show (Graphics g){fill(g, color);}

        public void hitsBall(){
            if (ball.loc.x + H < loc.x || ball.loc.x > loc.x + PW){
                life --;
                ball.init();
            }else{ //hit the paddle
                ball.dy = -ball.dy;
                ball.dx += paddle.boost();
            }
        }

        public int boost() {
            int cp = loc.x + PW / 2;
            System.out.println("Boost: " + (ball.loc.x + H/2 - cp) / 10);
            System.out.println("CP: ->" + cp);
            System.out.println("Ball_loc_x: ->" + ball.loc.x);

            return (ball.loc.x + H/2 - cp) / 10;
        }
    }
    //-------------------------Ball-----------------------//
    public static class Ball extends G.VS{
        public Color color = Color.WHITE;
        public static final int dy_start = -5;
        public int dx = 3, dy = dy_start;

        public Ball() {
            super(LEFT, BOT - 2 * H, H, H);
            init();
        }

        public void init(){
            paddle.dxS = PW / 2 - H / 2; //center of the paddle
            loc.set(paddle.loc.x + paddle.dxS, BOT - 2 * H);
            dy = dy_start;
            dx = 0;
        }

        public void  show(Graphics g){
            fill(g, color);
        }

        public void move() {
            if(paddle.dxS < 0){
                loc.x += dx;
                loc.y += dy;
                wallBounce();
                Brick.List.ballHitsBrick();
            }
        }
        public void wallBounce(){
            if (loc.x < LEFT){ loc.x = LEFT; dx = -dx;}
            if (loc.x + H > RIGHT ){ loc.x = RIGHT - H; dx = -dx;}

            if (loc.y < TOP){ loc.y = TOP; dy = -dy;}

            if (loc.y > BOT - 2 * H){
                paddle.hitsBall();
            }
        }
    }

    //----------------------Brick-----------------//
    public static class Brick extends G.VS {

        public static Color[] colors = {Color.red, Color.yellow, Color.CYAN, Color.orange, Color.magenta, Color.lightGray};
        public static List ALL = new List();
        public Color color;

        public Brick(int x, int y) {
            super(x, y, W, H);
            this.color = colors[G.rnd(colors.length)];
            ALL.add(this);
        }

        public void show(Graphics g) {
            fill(g, color);
            draw(g, Color.black);
        }

        public static void newBrickRows(int n){
            for(int i = 0; i < n; i ++){
                for (int j = 0; j < N_BRICK; j ++){
                    new Brick(LEFT + j * W, TOP + GAP + i * H);
                }
            }
        }

        public boolean hit (int x, int y){
            return  x < loc.x + W && (x + H) > loc.x
                    && y > loc.y  && y < (loc.y + H);
        }

        public void destroy() {
            ball.dy = - ball.dy;
            Brick.ALL.remove(this);
            score += 17;
            if (Brick.ALL.isEmpty()){
                startNewRows();
            }
        }

        //------------------List----------------//
        public static class List extends ArrayList<Brick>{

            public void show(Graphics g) {
                for (Brick b: this){
                    b.show(g);
                }
            }
            public static void ballHitsBrick(){
                int x = ball.loc.x, y = ball.loc.y;
                for (Brick b : ALL){
                    if (b.hit(x, y)){
                        b.destroy();
                        return;
                    }
                }
            }
        }
    }

}
