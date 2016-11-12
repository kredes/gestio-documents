package Domain;

import java.util.ArrayList;

public class CalcSimilitudCoseno implements CalcSimilitud {
    private static CalcSimilitud instance;

    public static CalcSimilitud getInstance() {
        if (instance == null) instance = new CalcSimilitudCoseno();
        return instance;
    }

    /* Constructor privado para que sólo pueda instanciarse desde la propia clase */
    private CalcSimilitudCoseno() {}

    @Override
    public double calculaSimilitud(Documento d1, Documento d2){
        ArrayList<Double> d1norm = Vector.getNormalizedArray(d1.getTermFreq(), d1.getNumeroPalabras());
        ArrayList<Double> d2norm = Vector.getNormalizedArray(d2.getTermFreq(), d2.getNumeroPalabras());

        return Vector.cosinus(d1norm,d2norm);
    }

}