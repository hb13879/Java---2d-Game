import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.shape.Path.*;
import javafx.animation.PathTransition.*;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Player {

  private Image sprite;
  private ImageView playerView;
  private int squareSize;
  private int PANEL;
  private final int MARGIN = 10;
  private final int BUFFER = 30;
  private final int ROTATE_TRANSITION_LEN = 100;
  private final int PLAYER_TRANSITION_LEN = 100;


  Player(int sqsize, int r, int c, int panel) {
    squareSize = sqsize;
    PANEL = panel;
    sprite = new Image("sprite.png");
    playerView = new ImageView();
    playerView.setImage(sprite);
    playerView.setPreserveRatio(true);
    playerView.setSmooth(true);
    playerView.setFitWidth(squareSize);
    playerView.setX(r*squareSize + MARGIN + PANEL);
    playerView.setY(c*squareSize + MARGIN);
  }

  void rotate(double degrees) {
    RotateTransition tx = new RotateTransition(Duration.millis(ROTATE_TRANSITION_LEN), playerView);
    tx.setFromAngle(playerView.getRotate());
    tx.setToAngle(degrees);
    tx.play();
    playerView.setRotate(degrees);
  }

  void move(int r, int c) {
    double current_x = playerView.getX();
    double current_y = playerView.getY();
    if(current_x == r*squareSize + PANEL && current_y == c*squareSize) {
      return;
    }
    Path path = new Path();
    MoveTo moveTo = new MoveTo(playerView.getX() + BUFFER,playerView.getY());
    playerView.setX(r*squareSize + PANEL);
    playerView.setY(c*squareSize);
    LineTo lineTo = new LineTo(playerView.getX() + BUFFER,playerView.getY() + BUFFER);
    path.getElements().add(moveTo);
    path.getElements().add(lineTo);
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(PLAYER_TRANSITION_LEN));
    pathTransition.setPath(path);
    pathTransition.setNode(playerView);
    pathTransition.play();
  }

  ImageView getPlayerView() {
    return playerView;
  }



  }
