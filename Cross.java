import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.text.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
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
import javafx.beans.property.SimpleIntegerProperty;

public class Cross extends Application {

  private Grid grid;
  private final int SIZE = 10;
  private final int MARGIN = 20;
  private final int BUFFER = 30;
  private final int PANEL = 200;
  private final String instructions =  "Collect as many coins as you can before time runs out! But be careful - don't run into any enemies...";
  private int squareSize;
  private int screenHeight;
  private int screenWidth;
  private int STARTTIME = 10;
  private int timeSeconds = STARTTIME;

  private Player player;
  private Coin coin;
  private Enemy enemy;
  //private Text timerText;
  private ImageView playerView;
  private ImageView coinView;
  private Timeline timeline;
  private Label timeLabel;
  private Label scoreLabel;
  private Label playAgain;
  private TextArea instruct;

  private GraphicsContext g;
  private Group root;
  private Canvas canvas;
  private GridPane pane;
  private GridPane endPane;
  private Scene scene;
  private List<Coin> coinList = new ArrayList<Coin>();
  private List<Enemy> enemyList = new ArrayList<Enemy>();
  private Button restart1;
  private Button quit;
  private Boolean gameOver = false;
  private Button restart2;

  public void start(Stage stage) {
    setup_grid();
    pane = new GridPane();
    endPane = new GridPane();
    initialise_game();
    adjust_pane(pane);
    adjust_endPane(endPane);
    root.getChildren().add(pane);
    root.getChildren().add(endPane);
    scene = new Scene(root);
    scene.getStylesheets().add("ass3Style.css");
    restart1.setOnAction(this::reset);
    restart2.setOnAction(this::reset);
    quit.setOnAction(this::quit);
    scene.setOnKeyReleased(this::move);
    setup_stage(stage);
    stage.show();
  }

  private void adjust_pane(GridPane pane) {
    pane.setPadding(new Insets(10));
    pane.setHgap(10);
    pane.setVgap(10);
    pane.setPrefWidth(PANEL + MARGIN);
    //pane.setGridLinesVisible(true);
  }

  private void adjust_endPane(GridPane endPane) {
    endPane.setPadding(new Insets(50));
    endPane.setLayoutX(150);
    endPane.setLayoutY(150);
    endPane.setHgap(50);
    endPane.setVgap(50);
    endPane.setPrefWidth(300);
    endPane.setVisible(false);
    endPane.getStyleClass().add("yellowButton");
  }

  private void setup_playAgain() {
    playAgain = new Label("Game Over - Play Again?");
    endPane.add(playAgain, 5,5,3,3);
    quit = new Button("Quit Game");
    endPane.add(quit, 5,8,3,3);
  }

  private void reset(ActionEvent e) {
    timeSeconds = STARTTIME;
    gameOver = false;
    timeline.stop();
    endPane.setVisible(false);
    grid.reset_game();
    timeLabel.setText("Time: " + timeSeconds);
    scoreLabel.setText("Score: " + grid.getScore());
    player.rotate(0);
    start_timer();
    draw_sprites();
  }

  private void quit(ActionEvent e) {
    Platform.exit();
  }

  private void setup_stage(Stage stage) {
    stage.setTitle("Game");
    stage.setScene(scene);
    stage.setFullScreen(true);
  }

  private void initialise_game() {
    initialise_score();
    initialise_time();
    setup_restart();
    setup_instructions();
    setup_playAgain();
    initialise_sprites();
    start_timer();
  }

  private void setup_instructions() {
    instruct = new TextArea(instructions);
    instruct.setEditable(false);
    instruct.setWrapText(true);
    instruct.getStyleClass().add("instructions");
    pane.add(instruct, 0, 9,2,9);
  }

  private void setup_restart() {
    restart1 = new Button("Restart");
    restart2 = new Button("Restart");
    restart1.getStyleClass().add("yellowButton");
    restart2.getStyleClass().add("yellowButton");
    pane.add(restart1, 0, 6,3,3);
    endPane.add(restart2,3,3,3,3);
  }

  private void setup_grid() {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN + PANEL, SIZE*squareSize + MARGIN);
    root = new Group();
    root.getChildren().add(canvas);
    g = canvas.getGraphicsContext2D();
    draw_background();
  }

  private void start_timer() {
    timeline = new Timeline();
    KeyFrame frame= new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>(){
      @Override public void handle(ActionEvent event) {
        timeSeconds--;
        timeLabel.setText("Time: " + timeSeconds);
        if(timeSeconds<=0 || gameOver){
          timeline.stop();
          end_game();
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

    Image scoreIcon = new Image("coin.png");
    ImageView scoreIconView = new ImageView();
    scoreIconView.setImage(scoreIcon);
    scoreIconView.setPreserveRatio(true);
    scoreIconView.setSmooth(true);
    scoreIconView.setFitWidth(squareSize);
    scoreLabel = new Label("Score: " + grid.getScore(), scoreIconView);
    scoreLabel.getStyleClass().add("scoreLabel");
    pane.add(scoreLabel, 0, 3,3,3);
  }

  private void initialise_time() {
    Image timerIcon = new Image("clock.png");
    ImageView timerIconView = new ImageView();
    timerIconView.setImage(timerIcon);
    timerIconView.setPreserveRatio(true);
    timerIconView.setSmooth(true);
    timerIconView.setFitWidth(squareSize);
    timeLabel = new Label("Time: " + timeSeconds, timerIconView);
    timeLabel.getStyleClass().add("timeLabel");
    pane.add(timeLabel, 0, 0, 3,3);
  }

  private void set_squaresize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = (int) screenSize.getHeight();
    screenWidth = (int) screenSize.getWidth();
    squareSize = (Math.min(screenHeight- MARGIN - BUFFER,screenWidth - MARGIN - BUFFER- PANEL)  )/SIZE;
  }


  private void move(KeyEvent e) {
    if(gameOver) {
      return;
    }
    if(e.getCode()==KeyCode.RIGHT) {
      grid.move(1,0);
      player.rotate(90);
    }
    else if(e.getCode()==KeyCode.LEFT) {
      grid.move(-1,0);
      player.rotate(270);
    }
    else if(e.getCode()==KeyCode.UP) {
      grid.move(0,-1);
      player.rotate(0);
    }
    else if(e.getCode()==KeyCode.DOWN) {
      grid.move(0,1);
      player.rotate(180);
    }
    scoreLabel.setText("Score: " + grid.getScore());
    draw_sprites();
    if(grid.gameOver()) {
      end_game();
    }
  }

  private void end_game() {
    gameOver = true;
    endPane.setVisible(true);
  }

  private void draw_sprites() {
    int i = 0;
    int j = 0;
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
        else if (k == 'E') {
          enemyList.get(j).move(r,c);
          j++;
        }
      }
    }
  }


  private void draw_background() {
    g.clearRect(0, 0, SIZE*squareSize + MARGIN + PANEL, SIZE*squareSize + MARGIN);
    g.setLineWidth(1);
    //draw vertical lines
    for (int x = MARGIN/2 + PANEL;x < SIZE*squareSize + MARGIN + PANEL; x += squareSize) {
      g.strokeLine(x,MARGIN/2,x,MARGIN/2+SIZE*squareSize);
    }
    //draw horizontal lines
    for (int y = MARGIN/2;y < SIZE*squareSize + MARGIN; y += squareSize) {
      g.strokeLine(MARGIN/2 + PANEL,y,MARGIN/2+SIZE*squareSize + PANEL,y);
    }
  }

  private void initialise_player(int r, int c) {
    player = new Player(squareSize,r,c, PANEL);
    root.getChildren().add(player.getPlayerView());
  }

  private void initialise_enemy(int r, int c) {
    enemy = new Enemy(squareSize,r,c, PANEL);
    root.getChildren().add(enemy.getPlayerView());
    enemyList.add(enemy);
  }

  private void initialise_coin(int r, int c) {
    coin = new Coin(squareSize,r,c, PANEL);
    root.getChildren().add(coin.getCoinView());
    coinList.add(coin);
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
