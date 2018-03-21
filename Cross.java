import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Cross extends Application {
  private Grid grid;
  private GraphicsContext g;
  private final int SIZE = 90;
  private final int MARGIN = 20;
  private final int BUFFER = 30;
  private int squareSize;
  private int screenHeight;
  private int screenWidth;

  public void start(Stage stage) {
    grid = new Grid(SIZE);
    set_squaresize();
    Canvas canvas = new Canvas(SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    Group root = new Group(canvas);
    Scene scene = new Scene(root);
    //scene.setOnMousePressed(this::move);
    stage.setTitle("Game");
    stage.setScene(scene);
    g = canvas.getGraphicsContext2D();
    draw();
    stage.show();
  }

  private void set_squaresize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = (int) screenSize.getHeight();
    screenWidth = (int) screenSize.getWidth();
    squareSize = (Math.min(screenHeight,screenWidth) - MARGIN - BUFFER)/SIZE;
  }

  // The current player makes a move in one of the cells.
/*  private void move(MouseEvent e) {
    int x = (int) e.getSceneX() / 100;
    int y = (int) e.getSceneY() / 100;
    grid.move(x, y);
    draw();
  }
*/
  // Redraw the current state of the grid, from scratch.
  private void draw() {
    g.clearRect(0, 0, SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    g.setLineWidth(1);
    drawVerticalLines();
    drawHorizontalLines();
    g.setLineWidth(3);
    for (int r=0; r<SIZE; r++) {
      for (int c=0; c<SIZE; c++) {
        char k = grid.get(r,c);
        if (k == 'O') drawO(squareSize*r + MARGIN/2, squareSize*c + MARGIN/2);
      }
    }
  }

  // Draw the grid lines.
  private void drawVerticalLines() {
    for (int x = MARGIN/2;x < SIZE*squareSize + MARGIN; x += squareSize) {
      g.strokeLine(x,MARGIN/2,x,MARGIN/2+SIZE*squareSize);
    }
  }

  private void drawHorizontalLines() {
    for (int y = MARGIN/2;y < SIZE*squareSize + MARGIN; y += squareSize) {
      g.strokeLine(MARGIN/2,y,MARGIN/2+SIZE*squareSize,y);
    }
  }

  // Draw an O
  private void drawO(double x, double y) {
    g.strokeOval(squareSize/8+x, squareSize/8+y, squareSize*0.75, squareSize*0.75);
  }

}
