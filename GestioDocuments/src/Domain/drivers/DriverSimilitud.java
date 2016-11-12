package Domain.drivers;

import Domain.*;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class DriverSimilitud {
    private static CalcSimilitud calcSimilitud;
    private static Scanner scanner;
    private static Collection c;

    public DriverSimilitud() {
        scanner = new Scanner(System.in);
        try {
            c = Collection.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    "Testeando el cálculo de similitud entre dos documentos (clases que implementan CalcSimilitud).\n" +
                            "   1. Similitud según frecuencia de palabras (clase CalcSimilitudFreq)\n" +
                            "   2. Similitud coseno (clase CalcSimilitudCoseno)\n" +
                            "   3. Similitud Tf-Idf + coseno (clase CalcSimilitudTfIdf)\n" +
                            "   0. Volver\n"
            );
            int seleccion = scanner.nextInt();
            scanner.nextLine();

            switch (seleccion) {
                case 0:
                    return;
                case 1:
                    calcSimilitud = CalcSimilitudFreq.getInstance();
                    break;
                case 2:
                    calcSimilitud = CalcSimilitudCoseno.getInstance();
                    break;
                case 3:
                    calcSimilitud = CalcSimilitudTfIdf.getInstance();
                    break;
                default:
                    calcSimilitud = CalcSimilitudFreq.getInstance();
                    System.out.println("Selección inválida. Usando calculador por defecto (frecuencia).");
                    break;
            }

            System.out.print("Id documento 1: ");
            int id1 = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Id documento 2: ");
            int id2 = scanner.nextInt();
            scanner.nextLine();

            Map<Integer, Documento> docs = c.getCollection();

            Documento d1 = docs.get(id1);
            Documento d2 = docs.get(id2);



            try {
                double similitud = calcSimilitud.calculaSimilitud(d1, d2);
                if (seleccion == 1) {
                    System.out.println(
                            String.format(
                                    "Los documentos %d ('%s') y %d ('%s') tienen %.0f palabras en común (normalizado a porcentaje en DriverCasosUso)",
                                    id1, d1.getTituloString(), id2, d2.getTituloString(), similitud
                            )
                    );
                } else {
                    System.out.println(
                            String.format("Los documentos %d ('%s') y %d ('%s') tienen una similitud de %f (%f%%)",
                                    id1, d1.getTituloString(), id2, d2.getTituloString(), similitud, similitud*100
                            )
                    );
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
