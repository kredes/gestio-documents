package Presentation;


import Domain.CalcSimilitud;
import Domain.CalcSimilitudFreq;
import Domain.Documento;
import Domain.MyPair;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class MainViewController extends ViewController {
    @FXML private HBox contentHBox;
    @FXML private VBox functionalityVBox;
    @FXML private VBox resultsVBox;
    @FXML private TextField buscarAutorTexto;
    @FXML private ListView listaResultados;

    private String ultimoAutorBuscado;
    private Label ultimoLabelItemPulsado;

    /* OVERRIDES */
    @Override
    public void afterShow() {
        listaResultados.setPrefHeight(scene.getHeight());
        functionalityVBox.setPrefWidth(contentHBox.getHeight()/3);
        resultsVBox.setPrefWidth(contentHBox.getHeight()*2/3);
    }

    @Override
    protected void errorControladorDominio() {

    }

    /* HANDLERS */
    @FXML private void buscarTitulosDeAutor() {
        String autor = buscarAutorTexto.getText();
        ArrayList<String> titulos = ctrlDominio.librosAutor(autor);

        if (titulos.isEmpty()) {
            listaResultados.getItems().clear();
        } else {
            ObservableList<Label> items = FXCollections.observableArrayList();
            for (String titulo : titulos) {
                Label label = new Label(titulo);

                label.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                        app.changeToDocumentView(titulo, autor);
                    }
                });

                addDocItemContextMenu(label, titulo, autor);
                label.onContextMenuRequestedProperty();
                items.add(label);
            }
            listaResultados.setItems(items);
        }
    }

    private void buscarSimilaresFreq(String titulo, String autor, int k, CalcSimilitud cs) {
        try {
            ArrayList<MyPair<Documento, Double>> similares = ctrlDominio.buscarParecidos(titulo, autor, k, cs);

            ObservableList<DocumentListItem> items = FXCollections.observableArrayList();

            //listaResultados.getItems().clear();

            for (MyPair<Documento, Double> pair : similares) {
                Documento doc = pair.getKey();

                DocumentListItem item = new DocumentListItem();

                item.setTitulo(doc.getTituloString());
                item.setAutores(String.join(",", doc.getAutoresStrings()));
                item.setTag(doc.getEtiquetasStrings().get(0));
                item.setPorcentaje(String.format("%.2f %% de similitud", pair.getValue()));

                addDocItemContextMenu(item, doc.getTituloString(), doc.getAutoresStrings().get(0));

                items.add(item);
            }

            listaResultados.setItems(items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* AUXILIARES */
    private void addDocItemContextMenu(Region region, String titulo, String autor) {
        ContextMenu contextMenu = new ContextMenu();
        Menu buscarSimilares = new Menu("Buscar similares");
        MenuItem similaresFreq = new MenuItem("Frecuencia");
        MenuItem similaresCoseno = new MenuItem("Coseno");
        MenuItem similaresTfIdf = new MenuItem("Tf-Idf");

        similaresFreq.setOnAction(event -> {
            StackPane pane = new StackPane();
            TextField text = new TextField();
            pane.getChildren().add(text);
            Scene scene = new Scene(pane, 200, 200);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.showAndWait();

            System.out.println(text.getText());
            int k = Integer.valueOf(text.getText());

            buscarSimilaresFreq(titulo, autor, k, CalcSimilitudFreq.getInstance());
        });

        buscarSimilares.getItems().addAll(similaresFreq, similaresCoseno, similaresTfIdf);
        contextMenu.getItems().add(buscarSimilares);

        region.setOnContextMenuRequested(event -> {
            contextMenu.show(region, event.getScreenX(), event.getScreenY());
        });
    }
}
