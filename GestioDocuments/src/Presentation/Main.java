package Presentation;/**
 * Created by kredes on 11/12/2016.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        changeToDocumentView();
    }

    public void changeToMainView() {
        try {
            MainViewController controller = changeScene("main_view.fxml");
            controller.setApp(this);
            controller.setUpView();

            stage.setTitle("GestioDocuments v0.0.0 - Main View");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeToDocumentView() {
        try {
            DocumentViewController controller = changeScene("document_view.fxml");
            controller.setApp(this);
            controller.setUpView();

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

        return (T) loader.getController();
    }

}
