import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Gridpane_test extends Application {
    @Override
    public void start(Stage stage) {
        Group root = new Group();
        Scene scene = new Scene(root, 500, 200);
        stage.setScene(scene);

        GridPane gridpane = new GridPane();
        for (int i = 0; i < 10; i++) {
            RowConstraints row = new RowConstraints(50);
            gridpane.getRowConstraints().add(row);
        }


        root.getChildren().add(gridpane);

        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
