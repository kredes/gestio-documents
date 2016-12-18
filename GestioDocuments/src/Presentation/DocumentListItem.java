package Presentation;

import Domain.Documento;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DocumentListItem extends VBox {
    @FXML private Label docItemTitulo;
    @FXML private Label docItemAutores;
    @FXML private Label docItemTag;
    @FXML private Label docItemPorcentaje;

    public DocumentListItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "document_list_item.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void fillWithoutPercentage(Documento doc) {
        tituloProperty().set(doc.getTituloString());
        autorProperty().set(String.join(",", doc.getAutoresStrings()));
        tagProperty().set(doc.getEtiquetasStrings().get(0));
        porcentajeProperty().set("");
    }

    public void fillWithPercentage(Documento doc, Double percentage) {
        fillWithoutPercentage(doc);
        porcentajeProperty().set(String.format("%.2f %% de similitud", percentage));
    }

    public StringProperty tituloProperty() {
        return docItemTitulo.textProperty();
    }
    public StringProperty autorProperty() {
        return docItemAutores.textProperty();
    }
    public StringProperty tagProperty() {
        return docItemTag.textProperty();
    }
    public StringProperty porcentajeProperty() {
        return docItemPorcentaje.textProperty();
    }

    public void setTitulo(String titulo) { tituloProperty().set(titulo); }
    public void setAutores(String autores) { autorProperty().set(autores); }
    public void setTag(String tag) { tagProperty().set(tag); }
    public void setPorcentaje(String porc) { porcentajeProperty().set(porc); }

    public String getAutoresString() { return docItemAutores.getText(); }

    public List<String> getAutores() {
        return new ArrayList<String>(Arrays.asList(docItemAutores.getText().split(",")));
    }

    public String getTag() { return docItemTag.getText(); }

}
