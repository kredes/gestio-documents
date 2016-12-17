package Presentation;/**
 * Created by kredes on 11/12/2016.
 */

import Domain.Collection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private Pane root = new Pane();
    private static Scene scene;
    private Stage stage;

    public Pane getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        System.out.print("Cargando colección... ");
        System.out.flush();
        try {
            Collection.getInstance();
        } catch (IOException e) {
            // Mostrar algo
        }
        System.out.println("hecho.");

        changeToMainView();
    }

    public void changeToMainView() {
        try {
            MainViewController controller = changeScene("main_view.fxml");
            controller.setApp(this);
            controller.setUpView();

            stage.setTitle("GestioDocuments v0.0.0 - Main View");
            stage.setScene(scene);
            stage.show();

            controller.afterShow();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeToDocumentView(String titulo, String autor) {
        try {
            DocumentViewController controller = changeScene("document_view.fxml");
            controller.setApp(this);
            controller.setUpView(titulo, autor);

            stage.setTitle("GestioDocuments - Document View [No editable]");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T changeScene(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource(fxml));

        Pane newRoot = null;
        try {
            newRoot = (Pane) loader.load();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        root = newRoot;
        scene = new Scene(root);
        scene.getStylesheets().add("Presentation/css/style.css");

        return (T) loader.getController();
    }

}
