package Domain;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class CalcSimilitudTfIdf implements CalcSimilitud {
    private static CalcSimilitud instance;

    public static CalcSimilitud getInstance() {
        if (instance == null) instance = new CalcSimilitudTfIdf();
        return instance;
    }

    /* Constructor privado para que sólo pueda instanciarse desde la propia clase */
    private CalcSimilitudTfIdf() {}

    private ArrayList<Double> vectorPeso1;
    private ArrayList<Double> vectorPeso2;

    public ArrayList<Double> getVPeso1(){ return vectorPeso1; }
    public ArrayList<Double> getVPeso2(){ return vectorPeso2; }



    @Override
    public double calculaSimilitud(Documento d1, Documento d2) throws IOException {
        vectorPeso1 = new ArrayList<>();
        vectorPeso2 = new ArrayList<>();
        ArrayList<Double> d1norm = Vector.getNormalizedArray(d1.getTermFreq(), d1.getNumeroPalabras());
        ArrayList<Double> d2norm = Vector.getNormalizedArray(d2.getTermFreq(), d2.getNumeroPalabras());

        Collection coll = Collection.getInstance();
        int nDocsTotal = coll.getNDocs();

        int i = 0;
        for (Map.Entry<Palabra, Double> entry : Collection.getInstance().getNDocsPerWord().entrySet()) {
            double nDocsOnSurt = entry.getValue();

            // inverse document frequency
            double idf;
            if (nDocsOnSurt == 0) idf = 0d;
            else idf = Math.log(nDocsTotal / nDocsOnSurt);

            // term frequency
            double tf1 = d1norm.get(i);
            double tf2 = d2norm.get(i);

            double pes1 = idf * tf1;
            double pes2 = idf * tf2;

            vectorPeso1.add(pes1);
            vectorPeso2.add(pes2);
            ++i;
        }

        return Vector.cosinus(vectorPeso1, vectorPeso2);
    }

}