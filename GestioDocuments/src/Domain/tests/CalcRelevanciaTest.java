package Domain.tests;

import Domain.CalcRelevancia;
import Domain.Documento;
import Domain.DocumentoNoExiste;
import Domain.MyPair;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CalcRelevanciaTest {
    @Test
    public void consultaRelevantes() throws IOException, DocumentoNoExiste {
        ArrayList<String> testStrings = new ArrayList<>(Arrays.asList(
                new String[]{
                        "Barcelona", "años"
                }
        ));
        ArrayList<MyPair<Documento, Double>> result = new CalcRelevancia().consultaRelevantes(testStrings, 20);
        System.out.println();
    }
}