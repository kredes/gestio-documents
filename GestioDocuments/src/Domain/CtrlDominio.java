package Domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface CtrlDominio {

    Documento buscarDocumento(String titulo, String autor) throws DocumentoNoExiste;

    ArrayList<String> librosAutor(String autor);

    ArrayList<String> autoresPorPrefijo(String prefijo);

    ArrayList<MyPair<Documento, Double>> buscarParecidos(String titulo, String autor, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException;

    Set<Documento> buscarExpresion(String expresion);

    ArrayList<Documento> buscarRelevantes(String query, int k);
}
