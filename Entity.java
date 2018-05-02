import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.shape.Path.*;
import javafx.animation.PathTransition.*;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Entity {

  protected Image sprite;
  protected ImageView entityView;
  protected int squareSize;
  protected int PANEL;
  protected final int MARGIN = 10;
  protected final int COLLAR = 50;
  protected final int ROTATE_TRANSITION_LEN = 100;

  Entity(int sqSize, int panel) {
    squareSize = sqSize;
    PANEL = panel;
    entityView = new ImageView();
    entityView.setPreserveRatio(true);
    entityView.setSmooth(true);
    entityView.setFitWidth(squareSize);
  }

  void rotate(double degrees) {
    RotateTransition tx = new RotateTransition(Duration.millis(ROTATE_TRANSITION_LEN), entityView);
    tx.setFromAngle(entityView.getRotate());
    tx.setToAngle(degrees);
    tx.play();
    entityView.setRotate(degrees);
  }

  void move(int r, int c) {
    entityView.setX(r*squareSize + COLLAR + PANEL);
    entityView.setY(c*squareSize + MARGIN);
  }

  ImageView getEntityView() {
    return entityView;
  }
}
