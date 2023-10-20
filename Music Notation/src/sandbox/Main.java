package sandbox;

import graphics.Window;

import java.awt.*;

public class Main {
    public static void main(String [] args){
        /*float acc = 0.0F;
        for (int i = 1; i <= 1000000; i++){
            acc += 1.0/ i;
        }
        System.out.println(acc);

        acc = 0.0F;
        for (int i = 1000000; i >= 1; i--){
            acc += 1.0/ i;
        }
        System.out.println(acc);*/

        /*Window.PANEL = new Paint();*/
        /*Window.PANEL = new Squares();*/
        /*Window.PANEL = new PaintInk();*/
        Window.PANEL = new ShapeTrainer();
        Window.launch();
        
    }
}
