package Domain;


import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by Alex on 11/11/2016.
 */
public class ObtencioTopWords {
    Map<String,Integer> m = new HashMap<>();


    private void addOrDieHard(String s) {
        if (!m.containsKey(s)) {
            m.put(s,1);
        }
        else {
            m.put(s,m.get(s)+1);
        }
    }

    public ObtencioTopWords() throws IOException {
        ArrayList<Documento> allDocumentos = ControladorPersistencia.getInstance().getAllDocumentos();

        for (int i = 0; i < 481; ++i) {
            Documento d = ControladorPersistencia.getInstance().getDocumento(i);
            for (Palabra p : d.getTitulo().getFrase()) {
                addOrDieHard(p.toString());
            }

            Contenido c = d.getArticulo();
            for (Frase f : c.getContenido()){
                for (Palabra p : f.getFrase()){
                    addOrDieHard(p.toString());
                }
            }

        }

        for (Map.Entry<String,Integer> e : m.entrySet()){
            System.out.println(e.getKey() + "\t" + e.getValue());
        }
    }
}
