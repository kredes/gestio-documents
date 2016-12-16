package Presentation;

import Domain.ControladorDominio;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by kredes on 16/12/2016.
 */
public abstract class ViewController {
    private Main app;
    private Scene scene;
    private Pane root;
    private ControladorDominio ctrlDominio;

    public ViewController() {
        try {
            ctrlDominio = ControladorDominio.getInstance();
        } catch (IOException e) {
            errorControladorDominio();
        }
    }

    public void setApp(Main app) {
        this.app = app;
    }

    public void setUpView() {
        root = app.getRoot();
        scene = app.getScene();
    }

    protected abstract void errorControladorDominio();
}
