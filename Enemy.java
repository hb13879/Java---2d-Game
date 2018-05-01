import javafx.scene.image.Image;

public class Enemy extends Entity {

  Enemy(int sqsize, int panel) {
    super(sqsize,panel);
    sprite = new Image("Tom.png");
    entityView.setImage(sprite);
  }
}
