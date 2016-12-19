package Domain.drivers;

import Domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class DriverCasosUso {

    private static Collection collection;
    private static ControladorDominio ctrlDominio;
    private static Scanner scanner;

    public DriverCasosUso() throws IOException {
        collection = Collection.getInstance();
        scanner = new Scanner(System.in);
        ctrlDominio = ControladorDominio.getInstance();
    }

    public static void run() {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    "Testeando el controlador de dominio (clase ControladorDominio).\n" +
                            "   1. Lista de títulos de un autor\n" +
                            "   2. Lista de autores que empiezan por un prefijo (que puede ser vacío): \n" +
                            "   3. Contenido de un documento dado su título y su autor: \n" +
                            "   4. Documentos similares dada una ID: \n" +
                            "   5. Documentos similares dados un título y un autor\n" +
                            "   0. Volver \n"
            );

            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Volver
                case 0:
                    return;

                //Lista de títulos de un autor
                case 1: {
                    System.out.print("Autors: ");
                    String autor = scanner.nextLine();
                    try {
                        System.out.println(
                                String.format(
                                        "ArrayList<String> = {%s}\n",
                                        String.join(", ", ctrlDominio.librosAutor(autor)))
                        );
                    } catch (AutorNoExiste e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Lista de autores que empiezan por un prefijo (que puede ser vacío)
                case 2: {
                    System.out.print("Prefijo: ");
                    String pref = scanner.nextLine();

                    System.out.print("Autores: ");
                    System.out.println(
                            String.format(
                                    "ArrayList<String> = {%s}\n",
                                    String.join(", ", ctrlDominio.autoresPorPrefijo(pref)))
                    );
                    break;
                }
                //Contenido de un documento dado su título y su autor
                case 3: {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();

                    try {
                        Documento d = ctrlDominio.buscarDocumento(titulo,autor);
                        System.out.print("Contenido: ");
                        System.out.print(d.getArticuloString());

                    } catch (DocumentoNoExiste e) {
                        System.out.print("Documento no existe \n");
                    }
                    break;
                }
                // Documentos similares (buscar por id)
                case 4: {
                    System.out.print("Id: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("k: ");
                    int k = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        Collection c = Collection.getInstance();
                        Map<String, Documento> index = c.getIndexID();
                        Documento doc = index.get(String.valueOf(id));

                        CalcSimilitud cs = getCalcSimilitud();

                        similares(doc, k, cs);

                    } catch (DocumentoNoExiste e) {
                        System.out.println("El documento no existe.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Documentos similares (buscar por título y autor)
                case 5: {
                    try {
                        System.out.print("Título: ");
                        String titulo = scanner.nextLine();

                        System.out.print("Autor: ");
                        String autor = scanner.nextLine();

                        System.out.print("k: ");
                        int k = scanner.nextInt();
                        scanner.nextLine();

                        Documento doc = ctrlDominio.buscarDocumento(titulo, autor);

                        CalcSimilitud cs = getCalcSimilitud();

                        similares(doc, k, cs);

                    }  catch (DocumentoNoExiste e) {
                        System.out.println("El documento no existe.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private static CalcSimilitud getCalcSimilitud() {
        System.out.println(
                "Selecciona método de cálculo de similitud (default - 1):\n" +
                        "   1. Frecuencia de palabras\n" +
                        "   2. Coseno\n" +
                        "   3. Tf-Idf + Coseno\n"
        );
        int tipoSimilitud = scanner.nextInt();
        scanner.nextLine();

        CalcSimilitud cs;

        if (tipoSimilitud == 1) cs = CalcSimilitudFreq.getInstance();
        else if (tipoSimilitud == 2) cs = CalcSimilitudCoseno.getInstance();
        else if (tipoSimilitud ==  3) cs = CalcSimilitudTfIdf.getInstance();
        else cs = CalcSimilitudFreq.getInstance();

        return cs;
    }

    private static void similares(Documento doc, int k, CalcSimilitud cs) throws DocumentoNoExiste, IOException {
        ArrayList<MyPair<Documento, Double>> parecidos = ctrlDominio.buscarParecidos(
                doc.getTituloString(), doc.getAutoresStrings().get(0), k, cs);
        ArrayList<String> titulos = new ArrayList<>();

        System.out.println(
                String.format("Documentos más similares, de mayor a menor similitud. (Seleccionado artículo: '%s' - Id: %d):",
                        doc.getTituloString(), doc.getId())
        );
        for (MyPair<Documento, Double> par : parecidos) {
            System.out.println(
                    String.format(
                            "\t- '%s' (%f%% de similitud)", par.getKey().getTituloString(), par.getValue()
                    )
            );
        }
        System.out.println();
    }
}