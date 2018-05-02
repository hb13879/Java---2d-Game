import javafx.scene.image.Image;
import javafx.scene.shape.*;
import javafx.scene.shape.Path.*;
import javafx.animation.PathTransition.*;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Player extends Entity {

  private final int BUFFER = 30;
  private final int PLAYER_TRANSITION_LEN = 100;

  Player(int sqsize, int panel) {
    super(sqsize,panel);
    sprite = new Image("sprite.png");
    entityView.setImage(sprite);
  }

@Override
  void move(int r, int c) {
    double current_x;
    double current_y;
    current_x = entityView.getX();
    current_y = entityView.getY();
    //check if at an edge/obstacle
    if(current_x == r*squareSize + PANEL + 50 + BUFFER && current_y == c*squareSize + MARGIN + BUFFER) {
      return;
    }
    Path path = new Path();
    MoveTo moveTo = new MoveTo(current_x ,current_y );
    entityView.setX(r*squareSize + PANEL + 50 + BUFFER);
    entityView.setY(c*squareSize + MARGIN + BUFFER*/);
    LineTo lineTo = new LineTo(entityView.getX(),entityView.getY());
    path.getElements().add(moveTo);
    path.getElements().add(lineTo);
    PathTransition pathTransition = new PathTransition();
    pathTransition.setDuration(Duration.millis(PLAYER_TRANSITION_LEN));
    pathTransition.setPath(path);
    pathTransition.setNode(entityView);
    pathTransition.play();
  }
}
