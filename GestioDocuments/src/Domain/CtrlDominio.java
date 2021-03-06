package Domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public interface CtrlDominio {

    Documento buscarDocumento(String titulo, String autor) throws DocumentoNoExiste;

    ArrayList<String> librosAutor(String autor) throws AutorNoExiste;

    ArrayList<String> autoresPorPrefijo(String prefijo);

    ArrayList<MyPair<Documento, Double>> buscarParecidos(String titulo, String autor, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException;

    ArrayList<MyPair<Documento, Double>> buscarParecidos(Documento doc, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException;

    Set<Documento> buscarExpresion(String expresion) throws SyntaxErrorException, IOException;

    ArrayList<MyPair<Documento, Double>> buscarRelevantes(String query, int k) throws IOException, DocumentoNoExiste;

    Documento anadirDocumento(String titulo, String autores, String etiqueta, String contenido) throws IOException;

    void modificarDocumento(Documento d) throws IOException;

    void eliminarDocumento(Documento d) throws IOException;
}
