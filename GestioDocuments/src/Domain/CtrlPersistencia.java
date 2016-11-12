package Domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

// TODO exceptions
public interface CtrlPersistencia {
    ArrayList<String> getTopWords() throws IOException;
    ArrayList<Documento> getAllDocumentos() throws IOException;
    int getNumDocumentos() throws IOException;
    void setNumDocumentos(int n) throws IOException;
    Documento getDocumento(int i) throws IOException;
    void guardaDocumento(Documento d, int id) throws IOException;
    Set<String> getStopWordsSet();
}
