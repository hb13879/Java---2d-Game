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
import java.util.*;
import javafx.animation.*;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;

public class Cross extends Application {
  private Grid grid;
  private GraphicsContext g;
  private final int SIZE = 10;
  private final int MARGIN = 20;
  private final int BUFFER = 30;
  private int squareSize;
  private int screenHeight;
  private int screenWidth;
  private int STARTTIME = 60;
  private Player player;
  private Coin coin;
  private Enemy enemy;
  private Text score;
  private Text timerText;
  private ImageView playerView;
  private ImageView coinView;
  private Group root;
  private Canvas canvas;
  private List<Coin> coinList = new ArrayList<Coin>();
  private Timeline timeline;
  private int timeSeconds = STARTTIME;

  public void start(Stage stage) {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    root = new Group();
    root.getChildren().add(canvas);
    g = canvas.getGraphicsContext2D();
    draw_background();
    initialise_score();
    initialise_time();
    start_timer();
    Scene scene = new Scene(root);
    initialise_sprites();
    scene.setOnKeyReleased(this::move);
    stage.setTitle("Game");
    stage.setScene(scene);
    stage.show();
    if(checkLose()) {
      try {
        Thread.sleep(100);
      }
      catch(Exception e){
        return;
      }
      Platform.exit();
    }
  }

  private void start_timer() {
    timeline = new Timeline();
    KeyFrame frame= new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
      @Override public void handle(ActionEvent event) {
        timeSeconds--;
        timerText.setText("Time: " + timeSeconds);
        if(timeSeconds<=0){
          timeline.stop();
        }
      }
    });
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(frame);
    if(timeline!=null){
      timeline.stop();
    }
    timeline.play();
  }

  private void initialise_score() {
    score = new Text(10,50,"Score: " + grid.getScore());
    root.getChildren().add(score);
  }

  private void initialise_time() {
    timerText = new Text(50,50,"Time: " + timeSeconds);
    root.getChildren().add(timerText);
  }

  private void initialise_coin(int r, int c) {
    coin = new Coin(squareSize,r,c);
    root.getChildren().add(coin.getCoinView());
    coinList.add(coin);
  }

  private void set_squaresize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = (int) screenSize.getHeight();
    screenWidth = (int) screenSize.getWidth();
    squareSize = (Math.min(screenHeight,screenWidth) - MARGIN - BUFFER)/SIZE;
  }


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
    timerText.setText("Time: " + timeSeconds);
    draw_sprites();
    checkLose();
    return;
  }

  private Boolean checkLose() {
    if(grid.gameOver()) {
      Text gameOver = new Text(100,500,"You Lose");
      root.getChildren().add(gameOver);
      return true;
    }
    return false;
  }

  private void draw_sprites() {
    int i = 0;
    for (int r=0; r<SIZE; r++) {
      for (int c=0; c<SIZE; c++) {
        char k = grid.get(r,c);
        if (k == 'O') {
          player.move(r,c);
        }
        else if (k == 'C') {
          coinList.get(i).move(r,c);
          i++;
        }
      }
    }
  }


  private void draw_background() {
    g.clearRect(0, 0, SIZE*squareSize + MARGIN, SIZE*squareSize + MARGIN);
    g.setLineWidth(1);
    //draw vertical lines
    for (int x = MARGIN/2;x < SIZE*squareSize + MARGIN; x += squareSize) {
      g.strokeLine(x,MARGIN/2,x,MARGIN/2+SIZE*squareSize);
    }
    //draw horizontal lines
    for (int y = MARGIN/2;y < SIZE*squareSize + MARGIN; y += squareSize) {
      g.strokeLine(MARGIN/2,y,MARGIN/2+SIZE*squareSize,y);
    }
  }

  private void initialise_player(int r, int c) {
    player = new Player(squareSize,r,c);
    root.getChildren().add(player.getPlayerView());
  }

  private void initialise_enemy(int r, int c) {
    enemy = new Enemy(squareSize,r,c);
    root.getChildren().add(enemy.getPlayerView());
  }

  private void initialise_sprites() {
    for (int r=0; r<SIZE; r++) {
      for (int c=0; c<SIZE; c++) {
        char k = grid.get(r,c);
        if (k == 'O') {
          initialise_player(r,c);
        }
        else if (k == 'C') {
          initialise_coin(r,c);
        }
        else if (k == 'E') {
          initialise_enemy(r,c);
        }
      }
    }
  }

}
