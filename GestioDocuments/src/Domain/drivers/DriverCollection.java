package Domain.drivers;

import Domain.Collection;
import Domain.Documento;
import Domain.Palabra;
import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedMap;

public class DriverCollection {

    private static Collection collection;
    private static Scanner scanner;
    private static ControladorPersistencia ctrlPersistencia;

    public DriverCollection() throws IOException {
        collection = Collection.getInstance();
        scanner = new Scanner(System.in);
        ctrlPersistencia = ControladorPersistencia.getInstance();
    }

    public static void run() throws IOException {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    //FUNCIONAN CASOS 1 y 6!! EL RESTO POR HACER!!
                    "Testeando la clase Collection de la capa de dominio (clase Collection).\n" +
                            "   1. Ver estado interno\n" +
                            "   2. Añadir documento\n" +
                            "   3. Modificar documento\n" +
                            "   4. Eliminar documento\n" +
                            "   0. Volver\n"
            );

            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Volver
                case 0:
                    return;
                // Ver estado interno
                // ID Docs Collection; TopWords;
                case 1: {
                    int nDocs = collection.getNDocs();
                    ArrayList<String> ids = new ArrayList<>();
                    for (SortedMap.Entry<Integer, Documento> entry: collection.getCollection().entrySet()) {
                        ids.add(String.valueOf(entry.getKey()));
                    }
                    System.out.print("TopWords (Hiperespacio): ");
                    System.out.println(
                            String.format(
                                    "List<String> topWords = {%s}\n",
                                    String.join(", ", collection.getTopWords()))
                    );

                    System.out.print("Nº de documentos donde aparece cada top word: ");
                    for (SortedMap.Entry<Palabra,Double> entry : collection.getNDocsPerWord().entrySet()) {
                        System.out.print(entry.getKey() + ": " + entry.getValue() + ", ");
                    }
                    System.out.println();
                    break;
                }
                //Añadir documento
                case 2: {
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Número de autores: ");
                    int nAutores = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Autores (uno por línea):");
                    ArrayList<String> autores = new ArrayList<>();
                    for (int i = 0; i < nAutores; ++i) {
                        autores.add(scanner.nextLine());
                    }
                    ;

                    System.out.print("Categoría: ");
                    ArrayList<String> tags = new ArrayList<>();
                    tags.add(scanner.nextLine());

                    System.out.println("Contenido (terminado por la cadena '~~'): ");
                    StringBuilder contenido = new StringBuilder();
                    String aux;
                    while (!(aux = scanner.nextLine()).equals("~~")) {
                        contenido.append(aux + "\n");
                    }

                    Documento d = new Documento(ctrlPersistencia.nextId(), titulo, autores, tags, contenido.toString());
                    try {
                        collection.afegirDoc(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Modificar documento
                case 3: {
                    System.out.print("Introduce la ID del documento a modificar: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nuevo título: ");
                    String titulo = scanner.nextLine();

                    System.out.print("Número de autores: ");
                    int nAutores = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Nuevos autores (uno por línea):");
                    ArrayList<String> autores = new ArrayList<>();
                    for (int i = 0; i < nAutores; ++i) {
                        autores.add(scanner.nextLine());
                    }
                    ;

                    System.out.print("Nueva categoría: ");
                    ArrayList<String> tags = new ArrayList<>();
                    tags.add(scanner.nextLine());

                    System.out.println("Nuevo contenido (terminado por la cadena '~~'): ");
                    StringBuilder contenido = new StringBuilder();
                    String aux;
                    while (!(aux = scanner.nextLine()).equals("~~")) {
                        contenido.append(aux + "\n");
                    }

                    try {
                        Documento d = ctrlPersistencia.getDocumento(id);
                        d.setTitulo(titulo);
                        d.setContent(contenido.toString());
                        d.setEtiquetas(tags);
                        d.setAutores(autores);
                        collection.modificarDoc(id,d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Eliminar documento
                case 4:
                    System.out.print("Introduce la ID del documento a eliminar: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        Documento d = ctrlPersistencia.getDocumento(id);
                        collection.eliminarDoc(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}