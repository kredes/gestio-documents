package Presentation;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

/**
 * Created by kredes on 11/12/2016.
 */
public class MainViewController {
    private Main app;
    private Scene scene;
    private Pane root;

    public void setApp(Main app) {
        this.app = app;
    }

    public void setUpView() {
        root = app.getRoot();
    }
}
