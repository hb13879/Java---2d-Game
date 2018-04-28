import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Coin {

  private Image sprite;
  private ImageView coinView;
  private int squareSize;
  private final int MARGIN = 14;

  Coin(int sqSize, int r, int c) {
    squareSize = sqSize;
    sprite = new Image("coin.png");
    coinView = new ImageView();
    coinView.setImage(sprite);
    coinView.setPreserveRatio(true);
    coinView.setSmooth(true);
    coinView.setFitWidth(squareSize*0.9);
    coinView.setX(r*squareSize + MARGIN);
    coinView.setY(c*squareSize + MARGIN);
  }

  ImageView getCoinView() {
    return coinView;
  }

  void move(int r, int c) {
    coinView.setX(r*squareSize + MARGIN);
    coinView.setY(c*squareSize + MARGIN);
  }

}
