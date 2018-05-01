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
  private ImageView playerView;
  private ImageView coinView;
  private Timeline timeline;
  private Label timeLabel;
  private Label scoreLabel;
  private Label playAgain;
  private TextArea instruct;

  private GraphicsContext g;
  private Scene scene;
  private Group root = new Group();
  private Canvas canvas;
  private GridPane sidebar = new GridPane();
  private GridPane gameOverPopup = new GridPane();
  private List<Coin> coinList = new ArrayList<Coin>();
  private List<Enemy> enemyList = new ArrayList<Enemy>();
  private Button restart1;
  private Button quit1;
  private Button quit2;
  private Boolean gameOver = false;
  private Button restart2;

  public void start(Stage stage) {
    scene = new Scene(root);
    scene.getStylesheets().add("ass3Style.css");
    adjust_stage(stage);
    setup_game();
    stage.show();
  }

  private void setup_game() {
    setup_grid();
    initialise_UI();
    create_sprites();
    setup_callbacks();
    start_timer();
  }

  private void initialise_UI() {
    create_UI_elements();
    style_UI_elements();
    adjust_UI_elements();
    layout_panels();
    style_panels();
    adjust_panels();
    populate_scene_graph();
  }

  private void populate_scene_graph() {
    root.getChildren().add(canvas);
    root.getChildren().add(gameOverPopup);
    root.getChildren().add(sidebar);
  }

  private void create_sprites() {
    player = new Player(squareSize, PANEL);
    root.getChildren().add(player.getPlayerView());
    for (int i = 0;i < grid.getCoins();i++) {
      coin = new Coin(squareSize, PANEL);
      coinList.add(coin);
      root.getChildren().add(coin.getCoinView());
    }
    for (int i = 0;i < grid.getCoins();i++) {
      enemy = new Enemy(squareSize, PANEL);
      enemyList.add(enemy);
      root.getChildren().add(enemy.getPlayerView());
    }
    draw_sprites();
  }


  private void setup_callbacks() {
    restart1.setOnAction(this::reset);
    restart2.setOnAction(this::reset);
    quit1.setOnAction(this::quit);
    quit2.setOnAction(this::quit);
    scene.setOnKeyReleased(this::move);
  }



  private void layout_panels() {
    sidebar.add(timeLabel, 0, 0, 3,3);
    sidebar.add(scoreLabel, 0, 3,3,3);
    sidebar.add(restart1, 0, 6,3,3);
    sidebar.add(instruct, 0, 9,3,9);
    sidebar.add(quit2,0,18,3,3);
    gameOverPopup.add(playAgain, 5,5,3,3);
    gameOverPopup.add(quit1, 5,8,3,3);
    gameOverPopup.add(restart2,3,3,3,3);
  }

  private void style_panels() {
    sidebar.getStyleClass().add("yellowButton");
    gameOverPopup.getStyleClass().add("yellowButton");
  }

  private void adjust_panels() {
    sidebar.setPadding(new Insets(10));
    sidebar.setHgap(10);
    sidebar.setVgap(10);
    sidebar.setPrefWidth(PANEL + MARGIN);
    //sidebar.setGridLinesVisible(true);
    gameOverPopup.setPadding(new Insets(50));
    gameOverPopup.setLayoutX(150);
    gameOverPopup.setLayoutY(150);
    gameOverPopup.setHgap(50);
    gameOverPopup.setVgap(50);
    gameOverPopup.setPrefWidth(300);
    gameOverPopup.setVisible(false);

  }

  private void create_UI_elements() {
    ImageView scoreIconView = assign_icon("coin.png");
    ImageView timerIconView = assign_icon("clock.png");
    scoreLabel = new Label("Score: " + grid.getScore(), scoreIconView);
    timeLabel = new Label("Time: " + timeSeconds, timerIconView);
    instruct = new TextArea(instructions);
    restart1 = new Button("Restart");
    restart2 = new Button("Restart");
    playAgain = new Label("Game Over - Play Again?");
    quit1 = new Button("Quit Game");
    quit2 = new Button("Quit Game");

  }

  private void style_UI_elements() {
    scoreLabel.getStyleClass().add("scoreLabel");
    timeLabel.getStyleClass().add("timeLabel");
    instruct.getStyleClass().add("instructions");
    restart1.getStyleClass().add("yellowButton");
    restart2.getStyleClass().add("yellowButton");
  }

  private void adjust_UI_elements() {
    instruct.setEditable(false);
    instruct.setWrapText(true);
  }

  private void setup_grid() {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN + PANEL, SIZE*squareSize + MARGIN);
    g = canvas.getGraphicsContext2D();
    draw_background();
  }

  private void reset(ActionEvent e) {
    timeSeconds = STARTTIME;
    gameOver = false;
    timeline.stop();
    gameOverPopup.setVisible(false);
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

  private void adjust_stage(Stage stage) {
    stage.setTitle("Game");
    stage.setScene(scene);
    stage.setFullScreen(true);
  }





  private void start_timer() {
    timeline = new Timeline();
    EventHandler<ActionEvent> onFinished = new EventHandler<ActionEvent>(){
      @Override public void handle(ActionEvent event) {
        timeSeconds--;
        timeLabel.setText("Time: " + timeSeconds);
        if(timeSeconds<=0 || gameOver){
          timeline.stop();
          end_game();
        }
      }
    };
    KeyFrame frame= new KeyFrame(Duration.seconds(1), onFinished);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(frame);
    if(timeline!=null){
      timeline.stop();
    }
    timeline.play();
  }

  private ImageView assign_icon(String icon) {
    Image iconPicture = new Image(icon);
    ImageView iconView = new ImageView();
    iconView.setImage(iconPicture);
    iconView.setPreserveRatio(true);
    iconView.setSmooth(true);
    iconView.setFitWidth(squareSize);
    return iconView;
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
    gameOverPopup.setVisible(true);
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

/*
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
  }*/

}
