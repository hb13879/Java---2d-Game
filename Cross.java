import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.Toolkit;
import java.awt.Dimension;

public class Cross extends Application {
  private Grid grid;
  private GraphicsContext g;
  private final int SIZE = 10;
  private final int MARGIN = 20;
  private final int BUFFER = 30;
  private int squareSize;
  private int screenHeight;
  private int screenWidth;
  private Text score;
  private Image player;
  private ImageView playerView;
  private ImageView coinView;
  private Group root;
  private Canvas canvas;

  public void start(Stage stage) {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    root = new Group();
    root.getChildren().add(canvas);
    g = canvas.getGraphicsContext2D();
    draw_background();
    initialise_score();
    Scene scene = new Scene(root);
    initialise_player();
    initialise_coins();
    scene.setOnKeyReleased(this::move);
    stage.setTitle("Game");
    stage.setScene(scene);
    stage.show();
  }

  private void initialise_score() {
    score = new Text(10,50,"Score: " + grid.getScore());
    root.getChildren().add(score);
  }

  private void initialise_coins() {
    player = new Image("coin.png");
    coinView = new ImageView();
    coinView.setImage(player);
    coinView.setPreserveRatio(true);
    coinView.setSmooth(true);
    coinView.setFitWidth(squareSize*0.9);
    root.getChildren().add(coinView);
    draw_sprites();
  }

  private void set_squaresize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = (int) screenSize.getHeight();
    screenWidth = (int) screenSize.getWidth();
    squareSize = (Math.min(screenHeight,screenWidth) - MARGIN - BUFFER)/SIZE;
  }

  // The current player makes a move in one of the cells.
  private void move(KeyEvent e) {
    if(e.getCode().getName().equals("Right")) {
      grid.move(1,0);
    }
    else if(e.getCode().getName().equals("Left")) {
      grid.move(-1,0);
    }
    else if(e.getCode().getName().equals("Up")) {
      grid.move(0,-1);
    }
    else if(e.getCode().getName().equals("Down")) {
      grid.move(0,1);
    }
    score.setText("Score: " + grid.getScore());
    draw_background();
    draw_sprites();
    return;
  }

  private void draw_background() {
    g.clearRect(0, 0, SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    g.setLineWidth(1);
    drawVerticalLines();
    drawHorizontalLines();
  }

  private void initialise_player() {
    player = new Image("sprite.png");
    playerView = new ImageView();
    playerView.setImage(player);
    playerView.setPreserveRatio(true);
    playerView.setSmooth(true);
    playerView.setFitWidth(squareSize);
    root.getChildren().add(playerView);
  }

  private void draw_sprites() {
    for (int r=0; r<SIZE; r++) {
      for (int c=0; c<SIZE; c++) {
        char k = grid.get(r,c);
        if (k == 'O') {
          playerView.setY(c*squareSize + 10);
          playerView.setX(r*squareSize + 10);
        }
        else if (k == 'C') {
          coinView.setY(c*squareSize + 15);
          coinView.setX(r*squareSize + 15);
        }
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

}
