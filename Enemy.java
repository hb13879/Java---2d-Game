import javafx.scene.image.Image;

public class Enemy extends Entity {

  Enemy(int sqsize, int panel) {
    super(sqsize,panel);
    sprite = new Image("Images/sprite.png");
    entityView.setImage(sprite);
  }
}
