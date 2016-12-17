package Presentation;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
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

    public DocumentListItem(String titulo, String autores, String tag) {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(Main.class.getResource("document_list_item.fxml"));

        loader.setController(this);

        try {
            loader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        docItemTitulo.setText(titulo);
        docItemAutores.setText(autores);
        docItemTag.setText(tag);
    }

    public String getTitulo() { return docItemTitulo.getText(); }

    public String getAutoresString() { return docItemAutores.getText(); }

    public List<String> getAutores() {
        return new ArrayList<String>(Arrays.asList(docItemAutores.getText().split(",")));
    }

    public String getTag() { return docItemTag.getText(); }

}
