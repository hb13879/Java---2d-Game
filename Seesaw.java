/* Display an animated drawing of a seesaw, using a canvas. */

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.animation.*;

public class Seesaw extends Application {
  private GraphicsContext g;
  private double angle;

  public void start(Stage stage) {
    Canvas canvas = new Canvas(400, 300);
    stage.setScene(new Scene(new Group(canvas)));
    g = canvas.getGraphicsContext2D();
    stage.show();
    timer.start();
  }

  AnimationTimer timer = new AnimationTimer() {
    public void handle(long now) {
      updateAngle(now);
      g.save();
      g.clearRect(0, 0, 400, 300);
      g.translate(200, 150);
      g.rotate(angle);
      g.fillRect(-150, -50, 300, 25);
      g.fillOval(-25, -25, 50, 50);
      g.restore();
    }
  };

  // Work out the angle from the time in nanoseconds.
  private void updateAngle(long now) {
    long billion = 1000000000;
    long seconds = now / billion;
    double fraction = (double) (now % billion) / billion;
    if ((seconds & 0x01) != 0) fraction = 1 - fraction;
    angle = -23 + fraction * 46;
  }
}
