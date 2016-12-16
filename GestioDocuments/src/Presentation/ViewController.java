package Presentation;

import Domain.ControladorDominio;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.IOException;


public abstract class ViewController {
    protected Main app;
    protected Scene scene;
    protected Pane root;
    protected ControladorDominio ctrlDominio;

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

    public void afterShow() {

    }

    protected abstract void errorControladorDominio();
}
