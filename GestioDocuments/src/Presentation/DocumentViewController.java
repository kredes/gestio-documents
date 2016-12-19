package Presentation;


import Domain.Documento;
import Domain.DocumentoNoExiste;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DocumentViewController extends ViewController {

    @FXML private TextField docViewTitulo;
    @FXML private TextField docViewAutores;
    @FXML private TextField docViewTag;
    @FXML private TextArea docViewContenido;
    @FXML private Button docViewEditar;
    @FXML private Button docViewGuardar;

    private boolean creandoDocumento;
    private Documento docActual;

    /* AUX */
    public void setUpView(String titulo, String autor) {
        super.setUpView();

        if (titulo != null && autor != null) {
            creandoDocumento = false;
            loadDocument(titulo, autor);
        } else {
            creandoDocumento = true;
            docActual = null;
            editarDocumento();
        }
    }

    @FXML
    private void handleBotonAtras() {
        app.changeToMainView();
    }

    @FXML private void editarDocumento() {
        docViewEditar.setDisable(true);
        docViewGuardar.setDisable(false);

        if (docViewAutores.getText().equals("(sin autor)")) docViewAutores.setText("");

        docViewTitulo.setEditable(true);
        docViewAutores.setEditable(true);
        docViewTag.setEditable(true);
        docViewContenido.setEditable(true);

        setDefaultStyle(docViewTitulo, docViewAutores, docViewTag, docViewContenido);
    }

    @FXML private void guardarDocumento() {
        docViewEditar.setDisable(false);
        docViewGuardar.setDisable(true);

        docViewTitulo.setEditable(false);
        docViewAutores.setEditable(false);
        docViewTag.setEditable(false);
        docViewContenido.setEditable(false);

        if (creandoDocumento) {
            try {
                Documento d = ctrlDominio.anadirDocumento(
                        docViewTitulo.getText(),
                        docViewAutores.getText(),
                        docViewTag.getText(),
                        docViewContenido.getText()
                );
                System.out.println(d.getId());
            } catch (IOException e) {
                popupError("IOException: " + e.getCause());
            }
        } else {
            try {
                docActual.setTitulo(docViewTitulo.getText());
                docActual.setAutores(new ArrayList<>(Arrays.asList(docViewAutores.getText().split(","))));
                docActual.setEtiquetas(new ArrayList<>(Arrays.asList(docViewTag.getText())));
                docActual.setContent(docViewContenido.getText());

                ctrlDominio.modificarDocumento(docActual);
            } catch (IOException e) {
                popupError("IOException: " + e.getCause());
            }
        }

        if (docViewAutores.getText().trim().equals("")) docViewAutores.setText("(sin autor)");

        setCustomStyle(docViewTitulo, docViewAutores, docViewTag, docViewContenido);
    }


    /* AUX */
    private void loadDocument(String titulo, String autor) {
        try {
            Documento doc = ctrlDominio.buscarDocumento(titulo, autor);
            docActual = doc;
            docViewTitulo.setText(doc.getTituloString());

            if (doc.getAutoresStrings().isEmpty() || doc.getAutoresStrings().get(0).equals("")) {
                docViewAutores.setText("(sin autor)");
            } else {
                docViewAutores.setText(String.join(",", doc.getAutoresStrings()));
            }

            docViewTag.setText(String.join(",", doc.getEtiquetasStrings()));
            docViewContenido.setText(doc.getArticuloString());
        } catch (DocumentoNoExiste e) {
            popupError("El documento no existe");
        }
    }

    private void setDefaultStyle(TextInputControl... controls) {
        for (TextInputControl control : controls) {
            control.getStyleClass().clear();

            if (control instanceof TextField) control.getStyleClass().addAll("text-field", "text-input");
            else if (control instanceof TextArea) control.getStyleClass().addAll("text-area", "text-input");
        }
    }

    private void setCustomStyle(TextInputControl... controls) {
        for (TextInputControl control : controls) {
            control.getStyleClass().clear();
            control.getStyleClass().add("textAreaAsLabel");
        }
    }
}
