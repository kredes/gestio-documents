import Domain.Collection;
import Domain.drivers.*;

import java.util.Scanner;

public class Main {

    private static DriverPersistencia driverPersistencia;
    private static DriverSimilitud driverSimilitud;
    private static DriverDocumentoContenidoFrasePalabra driverDoc;
    private static DriverCasosUso driverCasosUso;
    private static DriverCollection driverCollection;

    private static Scanner scanner;


    public static void main(String[] args) throws Exception {
        scanner = new Scanner(System.in);



        driverCasosUso = new DriverCasosUso();
        driverCollection = new DriverCollection();
        driverDoc = new DriverDocumentoContenidoFrasePalabra();
        driverPersistencia = new DriverPersistencia();
        driverSimilitud = new DriverSimilitud();

        System.out.print("Cargando colección... ");
        System.out.flush();
        Collection.getInstance();
        System.out.println("hecho.");

        //DriverExpresion.run();


        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    "Testeando drivers.\n" +
                            "   1. Driver Persistencia\n" +
                            "   2. Driver clases Documento, Contenido, Frase y Palabra\n" +
                            "   3. Driver calculadores de similitud (clases CalcSimilitudCoseno, CalcSimilitudFreq, CalcSimilitudTfIdf)\n" +
                            "   4. Driver casos de uso 1 (Crear/modificar/eliminar Documento)\n" +
                            "   5. Driver casos de uso 2 (Controlador dominio)\n" +
                            "   0. Salir\n"
            );
            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Salir
                case 0:
                    return;
                // Persistencia
                case 1:
                    driverPersistencia.run();
                    break;
                // Documento, Contenido, Frase y Palabra
                case 2:
                    driverDoc.run();
                    break;
                // Similitud
                case 3:
                    driverSimilitud.run();
                    break;
                // Colección
                case 4:
                    driverCollection.run();
                    break;
                // Casos de uso (controlador dominio)
                case 5:
                    driverCasosUso.run();
                    break;
            }
        }

    }

}
