package Domain;


import java.util.Iterator;
import java.util.Map;

public class CalcSimilitudFreq implements CalcSimilitud {
    private static CalcSimilitud instance;

    public static CalcSimilitud getInstance() {
        if (instance == null) instance = new CalcSimilitudFreq();
        return instance;
    }

    /* Constructor privado para que sólo pueda instanciarse desde la propia clase */
    private CalcSimilitudFreq() {}


    @Override
    public double calculaSimilitud(Documento d1, Documento d2) {
        Iterator<Map.Entry<Palabra,Double>> it = d1.getTermFreq().entrySet().iterator();
        Iterator<Map.Entry<Palabra,Double>> it2 = d2.getTermFreq().entrySet().iterator();

        int coincidencias = 0;
        Map.Entry<Palabra,Double> doc1;
        Map.Entry<Palabra,Double> doc2;

        while (it.hasNext()){
            doc1 = it.next();
            doc2 = it2.next();

            if (doc1.getValue() > 0 && doc2.getValue() > 0) ++coincidencias;
        }
        return coincidencias;
    }
}
