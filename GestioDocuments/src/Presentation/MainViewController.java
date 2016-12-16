package Presentation;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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

        ultimoAutorBuscado = autor;

        if (titulos.isEmpty()) {
            listaResultados.getItems().clear();
        } else {
            ObservableList<Label> items = FXCollections.observableArrayList();
            for (String titulo : titulos) {
                Label label = new Label(titulo);
                addDocItemContextMenu(label);
                items.add(label);
            }
            listaResultados.setItems(items);
        }
    }



    /* AUXILIARES */
    private void addDocItemContextMenu(Region region) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem verDoc = new MenuItem("Ver documento");
        Menu buscarSimilares = new Menu("Buscar similares");
        MenuItem similaresFreq = new MenuItem("Frecuencia");
        MenuItem similaresCoseno = new MenuItem("Coseno");
        MenuItem similaresTfIdf = new MenuItem("Tf-Idf");

        verDoc.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                app.changeToDocumentView(ultimoLabelItemPulsado.getText(), ultimoAutorBuscado);
            }
        });

        buscarSimilares.getItems().addAll(similaresFreq, similaresCoseno, similaresTfIdf);
        contextMenu.getItems().addAll(verDoc, buscarSimilares);

        region.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                ultimoLabelItemPulsado = (Label) event.getSource();
                contextMenu.show(region, event.getScreenX(), event.getScreenY());
            }
        });
    }
}
