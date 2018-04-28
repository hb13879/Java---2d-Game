import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Enemy {

  private Image sprite;
  private ImageView playerView;
  private int squareSize;
  private final int MARGIN = 10;

  Enemy(int sqsize, int r, int c) {
    squareSize = sqsize;
    sprite = new Image("Tom.png");
    playerView = new ImageView();
    playerView.setImage(sprite);
    playerView.setPreserveRatio(true);
    playerView.setSmooth(true);
    playerView.setFitWidth(squareSize);
    playerView.setX(r*squareSize + MARGIN);
    playerView.setY(c*squareSize + MARGIN);
  }

  void move(int r, int c) {
    playerView.setX(r*squareSize + MARGIN);
    playerView.setY(c*squareSize + MARGIN);
  }

  ImageView getPlayerView() {
    return playerView;
  }



}
