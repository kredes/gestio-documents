package Domain;


import java.util.ArrayList;
import java.util.Map;
import java.util.SortedMap;

public class Vector {

    public static ArrayList<Double> getNormalizedArray(SortedMap<Palabra, Double> tfD, double numPal){
        ArrayList<Double> arrayList = new ArrayList<>();
        for (Map.Entry<Palabra,Double> entry : tfD.entrySet()) {
            arrayList.add(entry.getValue()/numPal);
        }

        return arrayList;
    }

    /**
     * pre: a1.size() == a2.size()
     * post: el resultado es el coseno de a1 y a2
     */
    public static double cosinus(ArrayList<Double> a1, ArrayList<Double> a2){
        double suma = 0;
        double suma1cuad = 0;
        double suma2cuad = 0;

        int i = 0;
        for (Double d1 : a1) {
            double d2 = a2.get(i);
            suma += d1 * d2;
            suma1cuad += d1*d1;
            suma2cuad += d2*d2;
            ++i;
        }

        return suma / (Math.sqrt(suma1cuad)*Math.sqrt(suma2cuad));
    }

}
