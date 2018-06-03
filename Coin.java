import javafx.scene.image.Image;

public class Coin extends Entity {

  Coin(int sqsize, int panel) {
    super(sqsize,panel);
    sprite = new Image("Images/coin.png");
    entityView.setImage(sprite);
  }
}
