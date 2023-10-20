package game;

import game.Destructo.Grid;
import graphics.G;
import graphics.G.VS;
import graphics.Window;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StyledText extends Window {


  public static LayOut layout = new LayOut(100, 100,200,200);
  public static Font defaultFont1  = new Font("Helvetica", Font.PLAIN ,18);



  public StyledText(){
    super("StyledText", 1000, 500);
    Font f2  = new Font("TimesRoman", Font.PLAIN ,24);
    Style s2 = new Style(f2, Color.BLUE);
    Line line  = new Line(100);

//    Run r1 = new Run(100, line, defaultStyle);
//    Run r2 = new Run(200, line, s2);
//    r1.str = "Batman";
//    r2.str = "Rules!";
//    layout.add(r1);
//    layout.add(r2);
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();
//    layout.incIndent();


    layout.curr_run.str = "Indent Text";
    layout.newLine(1);
  }

    public void paintComponent(Graphics g){
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, 5000,5000);
      layout.show(g);
    }

    //----------------Box--------------------//
    public static class BOX {

      public G.VS vs;

      public BOX(int x, int y, int w, int h) {
        vs = new G.VS(x, y, w, h);
      }

      public int x1() {
        return vs.loc.x;
      }

      public int x2() {
        return vs.loc.x + vs.size.x;
      }

      public int y1() {
        return vs.loc.y;
      }

      public int y2() {
        return vs.loc.y + vs.size.y;
      }

      public void show (Graphics g){
        vs.fill(g, Color.LIGHT_GRAY);
      }
    }

    //---------------Style---------------------//
    public static class Style {

      public static Style defaultStyle = new Style(defaultFont1, Color.BLACK);

      public Font font;
      public Color color;

      public Style(Font font, Color color) {
        this.font = font;
        this.color = color;
      }

      public void set(Graphics g) {
        System.out.println("Setting: " + color);
        g.setFont(font);
        g.setColor(color);
        System.out.println("Setting: " + color);
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) {
          return true;
        }
        if (o == null || getClass() != o.getClass()) {
          return false;
        }
        Style style = (Style) o;
        return Objects.equals(font, style.font) && Objects.equals(color, style.color);
      }

      @Override
      public int hashCode() {
        return Objects.hash(font, color);
      }
    }
     //-------------Line------------------//
     public static class Line{
        public int y, a = 0, h = 0, leading = 4;
        public Line(int y){
          this.y = y;
        }
        public int yBaseLine(){return y + a;}
        public int yNextLine(){return y + h + leading;}
      }

      //-----------------Run----------------//
      public static class Run{ // a single style thing
        public String str;
        public Style style;
        public Line line;
        public int x1, x2;

        public Run(int x, Line line, Style style) {
          x1 = x; x2 = x;
          // Nothing on this line when we created it
          this.line = line; this.style = style;
        }
        public boolean isEmpty(){
          return str == "";

        }
        public void show(Graphics g){
          /**
           * We do not set the style here, the Style is set when we show a
           * list of runs
           */
          g.drawString(str, x1, line.yBaseLine());
        }
      }

      //-----------------LayOut(RunList)-----------------//
      public static class LayOut extends ArrayList<Run>{
        public Run curr_run; // current access to the previous run
        public BOX box;
        public int nIndent; // how many indents you've done
        public int wIndent = 0; // how many pixels you move per indent
        public boolean firstOnLine; // if curr is fist run on a new line


        public LayOut(int x, int y, int w, int h){
          box = new BOX(x, y, w, h);
          addRun(new Run(box.x1(), new Line(box.y1()), Style.defaultStyle));
          firstOnLine = true;
        }

        public void addRun(Run run){
          add(run);
          curr_run = run;
          firstOnLine = false;
        }
        public void incIndent(){ changeIndent(1);}
        public void decIndent(){ changeIndent(-1);}

        public void changeIndent(int delta){
          nIndent += delta;
          if (nIndent < 0){
            nIndent = 0;
          }

          if (firstOnLine && curr_run.isEmpty()){
            curr_run.x1 = xIndent();
            curr_run.x2 = xIndent();
          }
        }
        public int xIndent(){
          return box.x1() + nIndent * wIndent;
        }

        public void newLine(int delta){
          changeIndent(delta);
          Line line = new Line(curr_run.line.yNextLine());
          if (curr_run.isEmpty()) {
            curr_run.x1 = xIndent();
            curr_run.x2 = xIndent();
            curr_run.line = line;

          }else{
            addRun(new Run(xIndent(), line, curr_run.style));
          }
          firstOnLine =true;
        }

        public void show(Graphics g){

          Style cur_style = null; // current style
          box.show(g);
          System.out.println("Show list " + this.size());
          for(Run r : this){
            if (cur_style != r.style){
              cur_style = r.style;
              r.style.set(g);
            }
            r.show(g);
          }

        }
      }

      public static void main(String [] args){
        (PANEL = new StyledText()).launch();
      }
}
