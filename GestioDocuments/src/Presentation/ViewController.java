package Presentation;

import Domain.ControladorDominio;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
            popupError("Error al cargar el controlador de dominio:\n" + e.getCause());
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

    protected void popupError(String mensaje) {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(20));

        Label labelMensaje = new Label(mensaje);
        Button aceptar = new Button("Aceptar");
        aceptar.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(labelMensaje, aceptar);

        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(app.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        aceptar.setOnAction(event -> stage.close());

        stage.showAndWait();
    }

}
