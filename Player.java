import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.shape.Path.*;
import javafx.animation.PathTransition.*;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Player {

  private Image sprite;
  private ImageView playerView;
  private int squareSize;
  private final int MARGIN = 10;
  private final int BUFFER = 30;

  Player(int sqsize, int r, int c) {
    squareSize = sqsize;
    sprite = new Image("sprite.png");
    playerView = new ImageView();
    playerView.setImage(sprite);
    playerView.setPreserveRatio(true);
    playerView.setSmooth(true);
    playerView.setFitWidth(squareSize);
    playerView.setX(r*squareSize + MARGIN);
    playerView.setY(c*squareSize + MARGIN);
  }

  void move(int r, int c) {
    double current_x = playerView.getX();
    double current_y = playerView.getY();
    if(current_x == r*squareSize + MARGIN && current_y == c*squareSize + MARGIN) {
      return;
    }
    Path path = new Path();
    MoveTo moveTo = new MoveTo(playerView.getX() + BUFFER,playerView.getY() + BUFFER);
    playerView.setX(r*squareSize + MARGIN);
    playerView.setY(c*squareSize + MARGIN);
    LineTo lineTo = new LineTo(playerView.getX() + BUFFER,playerView.getY() + BUFFER);
    path.getElements().add(moveTo);
    path.getElements().add(lineTo);
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(100));
    pathTransition.setPath(path);
    pathTransition.setNode(playerView);
    pathTransition.play();
  }

  ImageView getPlayerView() {
    return playerView;
  }



  }
