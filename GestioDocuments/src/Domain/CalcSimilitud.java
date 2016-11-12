package Domain;


import java.io.IOException;

public interface CalcSimilitud {
    double calculaSimilitud(Documento d1, Documento d2) throws IOException;
}