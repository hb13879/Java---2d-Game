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

public class Display extends Application {

  private Grid grid;
  private final int SIZE = 10;
  private final int MARGIN = 20;                    //these parameters adjust various images in the game
  private final int BUFFER = 30;                    // ^^^^^^^^
  private final int PANEL = 300;                    // ^^^^^^^^
  private final int sidebarWidth = PANEL - MARGIN;
  private final String instructions =  "Collect as many coins as you can before time runs out! But be careful - don't run into any enemies...";
  private final int LEADERBOARDLENGTH = 5;          //how many scores are shown on the leaderboard
  private int squareSize,screenHeight,screenWidth;
  private int STARTTIME = 10;                       //start time - set very low for testing purposes
  private int timeSeconds = STARTTIME;
  private int min = 0;                              //minimum score required to make it onto leaderboard
  private Boolean gameOver = false;
  private String user;                              //name input by user to leaderboard
  //data structure for leaderboard uses an reverse ordered treemap to map each score to list of names who have achieved that
  private Map<Integer,List<String>> highscores = new TreeMap<>(Collections.reverseOrder());

  private Player player;
  private Coin coin;
  private Enemy enemy;
  private ImageView playerView,coinView;
  private Timeline timeline;
  private Label timeLabel, scoreLabel,playAgain,finalScore,congrats,leaderboardTitle;
  private TextArea instruct;
  private TextField userName;
  private List<Coin> coinList = new ArrayList<Coin>();
  private List<Enemy> enemyList = new ArrayList<Enemy>();
  private Button restart1,restart2,quit1,quit2,submit;
  private GridPane sidebar = new GridPane();
  private GridPane gameOverPopup = new GridPane();
  private GridPane leaderboard = new GridPane();
  private GridPane userNameForm = new GridPane();
  private Image bg,wood;
  private ImageView bgiv,woodiv;

  private Group root = new Group();
  private Scene scene = new Scene(root);
  private Canvas canvas;

  public void start(Stage stage) {
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

  //setup all UI elements apart from leaderboard
  private void initialise_UI() {
    create_UI_elements();
    style_UI_elements();
    adjust_UI_elements();
    layout_panels();
    style_panels();
    adjust_panels();
    populate_scene_graph();
  }

  //add relevant UI elements to appropriate nodes
  private void layout_panels() {
    sidebar.add(timeLabel, 0, 0, 3,3);
    sidebar.add(scoreLabel, 0, 3,3,3);
    sidebar.add(instruct, 0, 6,3,6);
    sidebar.add(restart1, 0, 12,3,3);
    sidebar.add(quit2,0,15,3,3);
    gameOverPopup.add(playAgain, 0,0,10,3);
    gameOverPopup.add(finalScore, 0, 1,10,3);
    gameOverPopup.add(quit1, 1,3,3,3);
    gameOverPopup.add(restart2,6,3,3,3);
    gameOverPopup.add(userName,2,2,2,2);
    leaderboard.add(leaderboardTitle,0,0,6,3);
    userNameForm.add(userName,0,3,3,3);
    userNameForm.add(congrats,0,0,9,3);
    userNameForm.add(submit,5,5,3,3);
  }

  //apply styles to nodes from stylesheet
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
    sidebar.setHalignment(timeLabel, HPos.CENTER);
    sidebar.setHalignment(scoreLabel, HPos.CENTER);
    sidebar.setHalignment(restart1, HPos.CENTER);
    sidebar.setHalignment(quit2, HPos.CENTER);
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
    leaderboard.setHalignment(leaderboardTitle, HPos.CENTER);
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
    leaderboardTitle = new Label("Leaderboard");
    congrats = new Label("Congratulations! You made the leaderboard \nPlease enter your name: ");
  }

  private void style_UI_elements() {
    scoreLabel.getStyleClass().add("scoreLabel");
    timeLabel.getStyleClass().add("timeLabel");
    finalScore.getStyleClass().add("finalScore");
    playAgain.getStyleClass().add("playAgain");
    instruct.getStyleClass().add("instructions");
    restart1.getStyleClass().add("yellowButton");
    restart2.getStyleClass().add("yellowButton");
    quit1.getStyleClass().add("yellowButton");
    quit2.getStyleClass().add("yellowButton");
    submit.getStyleClass().add("yellowButton");
    leaderboardTitle.getStyleClass().add("leaderboardTitle");
    congrats.getStyleClass().add("yellowButton");
  }

  private void adjust_UI_elements() {
    instruct.setEditable(false);
    instruct.setWrapText(true);
  }

  //add UI elements to scene graph
  private void populate_scene_graph() {
    root.getChildren().add(canvas);
    root.getChildren().add(gameOverPopup);
    root.getChildren().add(sidebar);
    root.getChildren().add(leaderboard);
    root.getChildren().add(userNameForm);
  }

  //initialise blank gameboard and draw background
  private void setup_grid() {
    grid = new Grid(SIZE, false);
    set_squaresize();
    canvas = new Canvas(SIZE*squareSize + MARGIN + PANEL, SIZE*squareSize + MARGIN);
    draw_background();
  }

  private void setup_callbacks() {
    restart1.setOnAction(this::reset);
    restart2.setOnAction(this::reset);
    quit1.setOnAction(this::quit);
    quit2.setOnAction(this::quit);
    scene.setOnKeyReleased(this::move);
    submit.setOnMousePressed(this::submit);
  }

  //used when name is submitted to leaderboard from form
  private void submit(MouseEvent e) {
    user = userName.getText();
    userNameForm.setVisible(false);
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

  //move player on arrow key pressed
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

  //on restart button
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

  //on quit game button
  private void quit(ActionEvent e) {
    Platform.exit();
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

  //create and start game timer
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
    KeyFrame frame = new KeyFrame(Duration.seconds(1), onFinished);
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.getKeyFrames().add(frame);
    if(timeline!=null){
      timeline.stop();
    }
    timeline.play();
  }

  //assign an icon to a label
  private ImageView assign_icon(String icon) {
    Image iconPicture = new Image(icon);
    ImageView iconView = new ImageView();
    iconView.setImage(iconPicture);
    iconView.setPreserveRatio(true);
    iconView.setSmooth(true);
    iconView.setFitWidth(squareSize);
    return iconView;
  }

  //calculate square size based on screen size and size of panel (sidebar)
  private void set_squaresize() {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    screenHeight = (int) screenSize.getHeight();
    screenWidth = (int) screenSize.getWidth();
    squareSize = (screenWidth - 2*PANEL - 50*2)/SIZE;
    //(Math.min(screenHeight- MARGIN - BUFFER,screenWidth - MARGIN - BUFFER- PANEL)  )/SIZE;
  }

  //on gameover
  private void end_game() {
    timeline.stop();
    gameOver = true;
    finalScore.setText("Your final score was: " + grid.getScore());
    gameOverPopup.setVisible(true);
    if(grid.getScore() > min) {
      userNameForm.setVisible(true);
    }
  }

  //create leaderboard, called on every game end
  private void populate_leaderboard() {
    int j = 0;
    leaderboard.getChildren().clear();
    leaderboard.add(leaderboardTitle,0,0,6,3);
    for(Map.Entry<Integer, List<String>> entry : highscores.entrySet()) {
      for(String name : entry.getValue()) {
        Label tmp = new Label(name + " " + entry.getKey());
        tmp.getStyleClass().add("leaderboardEntry");
        leaderboard.add(tmp,0,3 + (j*3),3,3);
        leaderboard.setHalignment(tmp, HPos.CENTER);
        if(j==LEADERBOARDLENGTH - 1) {
          min = entry.getKey();
          return;
        }
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
    wood = new Image("wood_panel.png");
    woodiv = new ImageView(wood);
    woodiv.setFitWidth(screenWidth);
    bg = new Image("forest_floor.png");
    bgiv = new ImageView(bg);
    bgiv.setX(PANEL + 50);
    bgiv.setY(0);
    bgiv.setSmooth(true);
    bgiv.setFitWidth(SIZE*squareSize);
    bgiv.setFitHeight(SIZE*squareSize);
    root.getChildren().add(woodiv);
    root.getChildren().add(bgiv);
    add_forest();
  }

  private void add_forest() {
    List<ImageView> flist = new ArrayList<>();
    for(int i = 0; i<3; i++) {
      Image forest = new Image("forest2.png");
      ImageView f = new ImageView(forest);
      f.setX(PANEL-50);
      f.setY(i*(screenHeight/3));
      f.setPreserveRatio(true);
      f.setSmooth(true);
      f.setFitWidth(150);
      root.getChildren().add(f);
    }
    for(int i = 0; i<3; i++) {
      Image forest = new Image("forest2.png");
      ImageView f = new ImageView(forest);
      f.setX(PANEL + SIZE*squareSize);
      f.setY(i*(screenHeight/3));
      f.setPreserveRatio(true);
      f.setSmooth(true);
      f.setFitWidth(150);
      root.getChildren().add(f);
    }
  }

  private void adjust_stage(Stage stage) {
    stage.setTitle("CoinHog");
    stage.setScene(scene);
    stage.setFullScreen(true);
  }
}
