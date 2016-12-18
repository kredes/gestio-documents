package Domain;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class CalcRelevancia {

    /**
     * Dadas p palabras (colectivamente denominadas query) y un entero k, obtener los k documentos mas relevantes para dicha query
     */
    public ArrayList<MyPair<Documento, Double>> consultaRelevantes(ArrayList<String> palabras, int k) throws IOException, DocumentoNoExiste {

        Collection.getInstance();

        String articulo = "";
        int i = palabras.size();
        for (String p : palabras) {
            for (int j = 0; j < i; ++j ) {
                articulo += p + " ";
            }
            --i;
        }
        Documento docFicticio = new Documento(-1, "", new ArrayList<>(), new ArrayList<>(), articulo);
        docFicticio.setFreqs(Collection.getInstance().getNDocsPerWord());

        CalcSimilitud calcSimilitud = CalcSimilitudTfIdf.getInstance();

        return ControladorDominio.getInstance().buscarParecidos(docFicticio, k, calcSimilitud);

    }
}