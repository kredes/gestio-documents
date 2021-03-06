package Presentation;


import Domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class MainViewController extends ViewController {
    @FXML private HBox contentHBox;
    @FXML private VBox functionalityVBox;
    @FXML private VBox resultsVBox;
    @FXML private TextField buscarAutorTexto;
    @FXML private TextField buscarPrefijoTexto;
    @FXML private TextField buscarDocTitulo;
    @FXML private TextField buscarDocAutor;
    @FXML private TextField buscarExpresionTexto;
    @FXML private TextField buscarQueryTexto;
    @FXML private TextField buscarQueryK;
    @FXML private ListView listaResultados;


    /* OVERRIDES */
    @Override
    public void afterShow() {
        listaResultados.setPrefHeight(scene.getHeight());
        functionalityVBox.setPrefWidth(scene.getWidth()/3);
        listaResultados.setPrefWidth(scene.getWidth()*2/3);
        resultsVBox.setPrefWidth(scene.getWidth()*2/3);
    }

    /* HANDLERS */
    @FXML private void buscarTitulosDeAutor() {
        buscarTitulosDeAutor(buscarAutorTexto.getText());
    }

    private void buscarTitulosDeAutor(String autor) {
        try {
            ArrayList<String> titulos = ctrlDominio.librosAutor(autor);

            if (titulos.isEmpty()) {
                listaResultados.getItems().clear();
            } else {
                ObservableList<Label> items = FXCollections.observableArrayList();
                for (String titulo : titulos) {
                    Label label = new Label(titulo);

                    setDocItemDobleClick(label, titulo, autor);
                    setDocItemContextMenu(label, titulo, autor);

                    label.onContextMenuRequestedProperty();
                    items.add(label);
                }
                listaResultados.setItems(items);
            }
        } catch (AutorNoExiste e) {
            popupError("El autor no existe");
        }
    }

    private void buscarSimilares(String titulo, String autor, int k, CalcSimilitud cs) {
        try {
            ArrayList<MyPair<Documento, Double>> similares = ctrlDominio.buscarParecidos(titulo, autor, k, cs);

            ObservableList<DocumentListItem> items = FXCollections.observableArrayList();

            if (similares.isEmpty()) {
                listaResultados.getItems().clear();
            }
            else {
                for (MyPair<Documento, Double> pair : similares) {
                    Documento doc = pair.getKey();
                    String tag = doc.getEtiquetasStrings().isEmpty() ? "" : doc.getEtiquetasStrings().get(0);
                    String primerAutor = doc.getAutoresStrings().isEmpty() ? "" : doc.getAutoresStrings().get(0);

                    DocumentListItem item = new DocumentListItem();
                    item.fillWithPercentage(doc, pair.getValue());

                    setDocItemDobleClick(item, doc.getTituloString(), primerAutor);
                    setDocItemContextMenu(item, doc.getTituloString(), primerAutor);

                    items.add(item);
                }
                listaResultados.setItems(items);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void buscarPorPrefijo() {
        String prefijo = buscarPrefijoTexto.getText();
        ArrayList<String> autores = ctrlDominio.autoresPorPrefijo(prefijo);

        ObservableList<Label> items = FXCollections.observableArrayList();
        for (String autor : autores) {
            Label item = new Label(autor);
            setAutorItemDobleClick(item, autor);
            items.add(item);
        }
        listaResultados.setItems(items);
    }

    @FXML private void buscarDocumento() {
        String titulo = buscarDocTitulo.getText();
        String autor = buscarDocAutor.getText();

        try {
            Documento doc = ctrlDominio.buscarDocumento(titulo, autor);

            DocumentListItem item = new DocumentListItem();
            item.fillWithoutPercentage(doc);

            ObservableList<DocumentListItem> items = FXCollections.observableArrayList();
            setDocItemDobleClick(item, titulo, autor);
            setDocItemContextMenu(item, titulo, autor);

            items.add(item);
            listaResultados.setItems(items);

        } catch (DocumentoNoExiste e) {
            listaResultados.getItems().clear();
            popupError("El documento no existe.");
        }
    }

    @FXML private void buscarExpresion() {
        String expresion = buscarExpresionTexto.getText();

        if (expresion.equals("")) {
            popupError("La expresión no puede estar vacía");
            return;
        }

        try {
            Set<Documento> docs = ctrlDominio.buscarExpresion(expresion);
            if (docs.isEmpty()) {
                listaResultados.getItems().clear();
                popupError("No se ha encontrado ninguna coincidencia");
            }
            else {
                ObservableList<DocumentListItem> items = FXCollections.observableArrayList();
                for (Documento doc : docs) {
                    DocumentListItem item = new DocumentListItem();
                    item.fillWithoutPercentage(doc);

                    String autor = doc.getAutoresStrings().isEmpty() ? "" : doc.getAutoresStrings().get(0);

                    setDocItemDobleClick(item, doc.getTituloString(), autor);
                    setDocItemContextMenu(item, doc.getTituloString(), autor);

                    items.add(item);
                }
                listaResultados.setItems(items);
            }
        } catch (SyntaxErrorException e) {
            popupError("Error sintáctico en la expresión");
        } catch (IOException e) {
            popupError("IOException: " + e.getCause());
        }
    }

    @FXML private void buscarRelevantes() {
        try {
            String query = buscarQueryTexto.getText();
            int k = Integer.valueOf(buscarQueryK.getText());

            if (k > Collection.getInstance().getNDocs()) k = Collection.getInstance().getNDocs();

            ArrayList<MyPair<Documento, Double>> relevantes = ctrlDominio.buscarRelevantes(query, k);

            ObservableList<DocumentListItem> items = FXCollections.observableArrayList();
            for (MyPair<Documento, Double> pair : relevantes) {
                if (pair.getValue().isNaN()) continue;

                Documento doc = pair.getKey();
                DocumentListItem item = new DocumentListItem();
                item.fillWithPercentage(doc, pair.getValue());

                String autor = doc.getAutoresStrings().isEmpty() ? "" : doc.getAutoresStrings().get(0);

                setDocItemDobleClick(item, doc.getTituloString(), autor);
                setDocItemContextMenu(item, doc.getTituloString(), autor);

                items.add(item);
            }
            if (items.isEmpty()) {
                popupError("No se han encontrado documentos relevantes");
                listaResultados.getItems().clear();
            } else {
                listaResultados.setItems(items);
            }

        } catch (NumberFormatException e) {
            popupError("Número inválido");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentoNoExiste e) {
            e.printStackTrace();
        }
    }

    private void eliminarDocumento(String titulo, String autor, Region region) {
        try {
            Documento doc = ctrlDominio.buscarDocumento(titulo, autor);
            ctrlDominio.eliminarDocumento(doc);

            if (region instanceof Label) {
                listaResultados.getItems().remove((Label) region);
            } else if (region instanceof DocumentListItem) {
                listaResultados.getItems().removeAll((DocumentListItem) region);
            }
        } catch (DocumentoNoExiste e) {
            popupError("El documento que quieres eliminar no existe");
        } catch (IOException e) {
            popupError("IOException: " + e.getCause());
        }
    }

    @FXML private void crearDocumento() {
        app.changeToDocumentView(null, null);
    }


    /* AUXILIARES */
    private void setDocItemDobleClick(Region region, String titulo, String autor) {
        region.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                app.changeToDocumentView(titulo, autor);
            }
        });
    }

    private void setAutorItemDobleClick(Region region, String autor) {
        region.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                buscarTitulosDeAutor(autor);
            }
        });
    }

    private void setDocItemContextMenu(Region region, String titulo, String autor) {
        ContextMenu contextMenu = new ContextMenu();
        Menu menuSimilares = new Menu("Buscar similares");
        MenuItem similaresFreq = new MenuItem("Frecuencia");
        MenuItem similaresCoseno = new MenuItem("Coseno");
        MenuItem similaresTfIdf = new MenuItem("Tf-Idf");
        MenuItem eliminarDoc = new MenuItem("Eliminar de la colección");

        similaresFreq.setOnAction(e -> buscarSimilares(titulo, autor, pedirNumeroPopUp(), CalcSimilitudFreq.getInstance()));
        similaresCoseno.setOnAction(e ->  buscarSimilares(titulo, autor, pedirNumeroPopUp(), CalcSimilitudCoseno.getInstance()));
        similaresTfIdf.setOnAction(e -> buscarSimilares(titulo, autor, pedirNumeroPopUp(), CalcSimilitudTfIdf.getInstance()));

        eliminarDoc.setOnAction(e -> eliminarDocumento(titulo, autor, region));

        menuSimilares.getItems().addAll(similaresFreq, similaresCoseno, similaresTfIdf);
        contextMenu.getItems().addAll(menuSimilares, eliminarDoc);

        region.setOnContextMenuRequested(event -> contextMenu.show(region, event.getScreenX(), event.getScreenY()));
    }


    private int pedirNumeroPopUp() {
        VBox pane = new VBox(10);
        pane.setPadding(new Insets(20));

        Label label = new Label("Inserta el número máximo de documentos a retornar (por defecto 10)");
        TextField numberField = new TextField();
        Button aceptar = new Button("Aceptar");
        pane.getChildren().addAll(label, numberField, aceptar);

        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initOwner(app.getStage());
        stage.initModality(Modality.WINDOW_MODAL);

        aceptar.setOnAction(event -> stage.close());

        stage.showAndWait();

        try {
            int k = Integer.valueOf(numberField.getText());
            if (k > Collection.getInstance().getNDocs()) k = Collection.getInstance().getNDocs();

            return k;
        } catch (Exception e) {
            return 10;
        }
    }
}
