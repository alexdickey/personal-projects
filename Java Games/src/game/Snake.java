package game;

import graphics.G;
import graphics.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Snake extends Window implements ActionListener {
    public static Color cFood = Color.green, cSnake = Color.blue, cBad = Color.red;
    public static Cell food = new Cell();
    public static Cell.List snake = new Cell.List();
    public static Cell crash = null;

    public static Timer timer;

    public Snake() {
        super("Snake", 1000, 700);
        startGame();
        timer = new Timer(100, this);
        timer.start();
    }

    private void startGame(){
        snake.clear(); // clear is the arraylist function
        snake.iHead = 0;
        snake.growList();
        food.rndLoc();

        crash = null;
    }
    public void keyPressed(KeyEvent ke){
        int vk = ke.getKeyCode();
        if (vk == KeyEvent.VK_LEFT){snake.direction = G.LEFT;}
        if (vk == KeyEvent.VK_RIGHT){snake.direction = G.RIGHT;}
        if (vk == KeyEvent.VK_UP){snake.direction = G.UP;}
        if (vk == KeyEvent.VK_DOWN){snake.direction = G.DOWN;}
        if (vk == KeyEvent.VK_SPACE){
            startGame();
        }
        if (vk == KeyEvent.VK_A){
            snake.growList();
        }
//        repaint();
    }
    public static void moveSnake(){
        if (crash != null){
            return;
        }
        snake.move();
        Cell head = snake.head();
        if (head.hits(food)){
            snake.growList();
            food.rndLoc();
            return;
        }
        if(!head.inBoundary()){
            crash = head;
            snake.stop();
            return;
        }
        if(snake.hits(head)){
            crash = head;
            snake.stop();
            return;
        }
    }

    public void paintComponent(Graphics g){
        G.clear(g);
        g.setColor(cSnake);
        snake.show(g);
        g.setColor(cFood); food.show(g);
        if(crash != null){
            g.setColor(cBad);
            crash.show(g);
        }
        Cell.drawBoundary(g);
    }

    public static void main(String[] args){
        (PANEL = new Snake()).launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveSnake();
        repaint();
    }

    //-------------------------Cell--------------------------//
    public static class Cell extends G.V { // the location where the cell is

        public static final int xM = 35, yM = 35, nX = 30, nY = 20, W = 30;
        public Cell() {
            super(G.rnd(nX), G.rnd(nY));
        }

        public Cell(Cell c){
            super(c.x, c.y);
        }

        public void show(Graphics g){
            g.fillRect(xM + x * W, yM + y * W, W, W);
        }
        public void rndLoc(){
            set(G.rnd(nX), G.rnd(nY)); // set the cell loc to be random;
        }

        public static void drawBoundary(Graphics g){
            int xMax = xM + nX * W, yMax = yM + nY * W;
            g.setColor(Color.black);
            g.drawLine(xM, yM, xM, yMax); // yM to yMax on same xM
            g.drawLine(xMax, yM, xMax,yMax);// yM to yMax on same xMax

            g.drawLine(xM, yM, xMax, yM); // xM to xMax on same yM
            g.drawLine(xM, yMax,  xMax,yMax); // xM to xMax one same yMax
        }

        public boolean hits(Cell c) {
            return c.x == x && c.y == y;
        }

        public boolean inBoundary() {
            return x >= 0 && x < nX && y >=0 && y < nY;
        }

        //-------------------------List---------------------------//
        public static class List extends ArrayList<Cell>{//Snake -> Cell -> List

            public static G.V STOPPED = new G.V(0, 0); // not moving
            public G.V direction = STOPPED;

            public int iHead = 0; // the head of the snake

            public void show(Graphics g) {
                for (Cell c : this){
                    c.show(g);
                }
            }
            public void growList(){
                Cell cell = (size() == 0) ? new Cell() : new Cell(get(0)); // first cell on the list
                add(cell); // adding the new cell on the snake.
            }

            public void move(){ // move snake by one element
                if (direction == STOPPED){return;}
                int iTail = (iHead + 1) % size();
                Cell tail = get(iTail); // if iHead + 1 = size, set iTail to zero;
                tail.set(get(iHead)); // Tail becomes the Head
                tail.add(direction);
                iHead = iTail; //new head is the former tail
            }


            public Cell head() {
                return get(iHead);
            }

            public void stop() {
                direction = STOPPED;
            }

            public boolean hits(Cell target) {
                for (Cell c : this){
                    if (c != target  && c.hits(target)){
                        return true;
                    }
                }
                return false;
            }
        }
    }


}
