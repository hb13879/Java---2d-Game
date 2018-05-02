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
  private final int PANEL = 300;
  private final int sidebarWidth = PANEL - MARGIN;
  private final String instructions =  "Collect as many coins as you can before time runs out! But be careful - don't run into any enemies...";
  private int squareSize,screenHeight,screenWidth;
  private int STARTTIME = 10;
  private int timeSeconds = STARTTIME;
  private Boolean gameOver = false;
  private String user;
  private Map<Integer,List<String>> highscores = new TreeMap<>();

  private Player player;
  private Coin coin;
  private Enemy enemy;
  private ImageView playerView,coinView;
  private Timeline timeline;
  private Label timeLabel, scoreLabel,playAgain,finalScore,congrats;
  private TextArea instruct;
  private TextField userName;
  private List<Coin> coinList = new ArrayList<Coin>();
  private List<Enemy> enemyList = new ArrayList<Enemy>();
  private Button restart1,restart2,quit1,quit2,submit;
  private GridPane sidebar = new GridPane();
  private GridPane gameOverPopup = new GridPane();
  private GridPane leaderboard = new GridPane();
  private GridPane userNameForm = new GridPane();
  private List<Label> leaderboardList = new ArrayList<Label>();

  private Group root = new Group();
  private Scene scene = new Scene(root);
  private Canvas canvas;

  public void start(Stage stage) {
    //scene = new Scene(root);
    scene.getStylesheets().add("ass3Style.css");
    adjust_stage(stage);
    setup_game();
    stage.show();
  }

  private void setup_game() {
    setup_grid();
    create_sprites();
    initialise_UI();
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
    root.getChildren().add(leaderboard);
    root.getChildren().add(userNameForm);
  }

  private void create_sprites() {
    //create player
    player = new Player(squareSize, PANEL);
    root.getChildren().add(player.getEntityView());
    //create coins
    for (int i = 0;i < grid.getCoins();i++) {
      coin = new Coin(squareSize, PANEL);
      coinList.add(coin);
      root.getChildren().add(coin.getEntityView());
    }
    //create enemies
    for (int i = 0;i < grid.getCoins();i++) {
      enemy = new Enemy(squareSize, PANEL);
      enemyList.add(enemy);
      root.getChildren().add(enemy.getEntityView());
    }
    draw_sprites();
  }


  private void setup_callbacks() {
    restart1.setOnAction(this::reset);
    restart2.setOnAction(this::reset);
    quit1.setOnAction(this::quit);
    quit2.setOnAction(this::quit);
    scene.setOnKeyReleased(this::move);
    submit.setOnMousePressed(this::submit);
  }

  private void layout_panels() {
    sidebar.add(timeLabel, 0, 0, 3,3);
    sidebar.add(scoreLabel, 0, 3,3,3);
    sidebar.add(restart1, 0, 6,3,3);
    sidebar.add(instruct, 0, 9,3,9);
    sidebar.add(quit2,0,18,3,3);
    gameOverPopup.add(playAgain, 0,0,10,3);
    gameOverPopup.add(finalScore, 0, 1,10,3);
    gameOverPopup.add(quit1, 1,3,3,3);
    gameOverPopup.add(restart2,6,3,3,3);
    gameOverPopup.add(userName,2,2,2,2);
    userNameForm.add(userName,0,3,3,3);
    userNameForm.add(congrats,0,0,9,3);
    userNameForm.add(submit,5,5,3,3);
  }

  private void style_panels() {
    sidebar.getStyleClass().add("yellowButton");
    gameOverPopup.getStyleClass().add("yellowButton");
    leaderboard.getStyleClass().add("yellowButton");
    userNameForm.getStyleClass().add("yellowButton");
  }

  private void adjust_panels() {
    sidebar.setPadding(new Insets(10));
    sidebar.setHgap(10);
    sidebar.setVgap(10);
    sidebar.setPrefHeight(screenHeight);
    sidebar.setPrefWidth(sidebarWidth);
    //sidebar.setGridLinesVisible(true);
    gameOverPopup.setPadding(new Insets(50));
    gameOverPopup.setLayoutX(PANEL + BUFFER);
    gameOverPopup.setLayoutY(50);
    gameOverPopup.setHgap(50);
    gameOverPopup.setVgap(50);
    gameOverPopup.setAlignment(Pos.CENTER);
    gameOverPopup.setPrefWidth(600);
    gameOverPopup.setPrefHeight(200);
    gameOverPopup.setVisible(false);
    gameOverPopup.setHalignment(finalScore, HPos.CENTER);
    gameOverPopup.setHalignment(playAgain, HPos.CENTER);
    //gameOverPopup.setGridLinesVisible(true);
    leaderboard.setLayoutX(screenWidth - sidebarWidth);
    leaderboard.setPrefWidth(sidebarWidth);
    leaderboard.setPrefHeight(screenHeight);
    userNameForm.setLayoutX(PANEL + BUFFER);
    userNameForm.setLayoutY(50);
    userNameForm.setAlignment(Pos.CENTER);
    userNameForm.setPrefWidth(600);
    userNameForm.setPrefHeight(200);
    userNameForm.setVisible(false);
    userNameForm.setHalignment(congrats, HPos.CENTER);
  }

  private void create_UI_elements() {
    ImageView scoreIconView = assign_icon("coin.png");
    ImageView timerIconView = assign_icon("clock.png");
    scoreLabel = new Label("Score: " + grid.getScore(), scoreIconView);
    timeLabel = new Label("Time: " + timeSeconds, timerIconView);
    finalScore = new Label("Your final score was: " + grid.getScore());
    playAgain = new Label("Game Over - Play Again?");
    instruct = new TextArea(instructions);
    restart1 = new Button("Restart");
    restart2 = new Button("Restart");
    quit1 = new Button("Quit Game");
    quit2 = new Button("Quit Game");
    userName = new TextField();
    submit = new Button("Submit");
    congrats = new Label("Congratulations! You made the leaderboard \n Please enter your name: ");
  }

  private void style_UI_elements() {
    scoreLabel.getStyleClass().add("scoreLabel");
    timeLabel.getStyleClass().add("timeLabel");
    finalScore.getStyleClass().add("finalScore");
    playAgain.getStyleClass().add("playAgain");
    instruct.getStyleClass().add("instructions");
    restart1.getStyleClass().add("yellowButton");
    restart2.getStyleClass().add("yellowButton");
    submit.getStyleClass().add("yellowButton");
    congrats.getStyleClass().add("yellowButton");
  }

  private void adjust_UI_elements() {
    instruct.setEditable(false);
    instruct.setWrapText(true);
  }

  private void setup_grid() {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN + PANEL, SIZE*squareSize + MARGIN);
    draw_background();
  }

  private void submit(MouseEvent e) {
    user = userName.getText();
    userNameForm.setVisible(false);
    grid.adjust_leaderboard();
    if(highscores.containsKey(grid.getScore())) {
      highscores.get(grid.getScore()).add(user);
    }
    else {
      List<String> l = new ArrayList<>();
      l.add(user);
      highscores.put(grid.getScore(),l);
    }
    populate_leaderboard();
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
        if(timeSeconds<=0){
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
    timeline.stop();
    gameOver = true;
    finalScore.setText("Your final score was: " + grid.getScore());
    gameOverPopup.setVisible(true);
    if(grid.getScore() > grid.getMin()) {
      userNameForm.setVisible(true);
    }
  }

  private void populate_leaderboard() {
    int j = 0;
    leaderboard.getChildren().clear();
    for(int i = 0;i<highscores.size();i++) {
      int scoretmp = grid.getLeader(i);
      for(String name : highscores.get(scoretmp)) {
        Label tmp = new Label(name + " " + scoretmp);
        tmp.getStyleClass().add("yellowButton");
        leaderboard.add(tmp,0,(j*3),3,3);
        leaderboardList.add(tmp);
        j++;
      }
    }
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
    GraphicsContext g = canvas.getGraphicsContext2D();
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

}
