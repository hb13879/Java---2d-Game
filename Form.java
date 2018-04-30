/* Example of a form layout using GridPane. */

import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;

public class Form extends Application {
  private Label what;
  private TextField text;
  private CheckBox match, wrap, words, back;
  private Button find, cancel;

  public void start(Stage stage) {
    GridPane pane = new GridPane();
    create();
    layout(pane);
    adjust(pane);
    Scene scene = new Scene(pane);
    stage.setScene(scene);
    stage.setTitle("Form");
    stage.show();
  }

  private void create() {
    what = new Label("Find what:");
    text = new TextField("");
    match = new CheckBox("Match Case");
    wrap = new CheckBox("Wrap Around");
    words = new CheckBox("Whole words");
    back = new CheckBox("Search Backwards");
    find = new Button("Find");
    cancel = new Button("Cancel");
  }

  private void layout(GridPane pane) {
    pane.add(what, 0, 0);
    pane.add(text, 1, 0, 2, 1);
    pane.add(match, 1, 1);
    pane.add(wrap, 2, 1);
    pane.add(words, 1, 2);
    pane.add(back, 2, 2);
    pane.add(find, 3, 0);
    pane.add(cancel, 3, 1, 1, 2);
  }

  private void adjust(GridPane pane) {
    pane.setPadding(new Insets(10));
    pane.setHgap(10);
    pane.setVgap(10);
    find.setMaxWidth(300);
    cancel.setMaxWidth(300);
  }
}
