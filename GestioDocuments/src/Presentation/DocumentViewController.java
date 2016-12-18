package Presentation;


import Domain.Documento;
import Domain.DocumentoNoExiste;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class DocumentViewController extends ViewController {

    @FXML private Label docViewTitulo;
    @FXML private Label docViewAutores;
    @FXML private Label docViewTag;
    @FXML private TextArea docViewContenido;
    @FXML private Button docViewEditar;
    @FXML private Button docViewGuardar;

    /* OVERRIDE */
    @Override
    protected void errorControladorDominio() {

    }

    /* AUX */
    public void setUpView(String titulo, String autor) {
        super.setUpView();

        try {
            Documento doc = ctrlDominio.buscarDocumento(titulo, autor);
            docViewTitulo.setText(doc.getTituloString());
            docViewAutores.setText(String.join(",", doc.getAutoresStrings()));
            docViewTag.setText(String.join(",", doc.getEtiquetasStrings()));
            docViewContenido.setText(doc.getArticuloString());
        } catch (DocumentoNoExiste e) {
            // Lo que sea
        }
    }

    @FXML
    private void handleBotonAtras() {
        app.changeToMainView();
    }

    @FXML private void editarDocumento() {
        docViewEditar.setDisable(true);
        docViewTitulo.setDisable(false);
        docViewAutores.setDisable(false);
        docViewTag.setDisable(false);
        docViewContenido.setDisable(false);
        docViewGuardar.setDisable(false);
    }

    @FXML private void guardarDocumento() {

    }
}
