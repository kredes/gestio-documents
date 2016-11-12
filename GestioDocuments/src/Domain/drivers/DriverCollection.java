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
                            "   5. Lista Títulos por Autor\n" +
                            "   6. Lista autores prefijo\n" +
                            "   7. Contenido Doc, dado Título + Autor\n" +
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
                    System.out.println(
                            String.format(
                                    "List<Integer> docIDs = {%s}\n" +
                                            "Estos son los ID de los documentos que hay en Collection\n",
                                    String.join(", ", ids))
                    );

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
                // Buscar títulos por Autor
                case 5:
                    System.out.print("Autor: ");
                    String autor = scanner.nextLine();
                    ArrayList<String> titulos = new ArrayList<>();
                    if (!collection.getIndexAutor().containsKey(autor)) System.out.println("No existe este autor");
                    else {
                        for (Documento d : collection.getIndexAutor().get(autor)) {
                            titulos.add(d.getTituloString());
                        }

                        System.out.print("Títulos: ");
                        System.out.println(
                                String.format(
                                        "ArrayList<String> = {%s}\n",
                                        String.join(", ", titulos))
                        );
                    }
                    break;
                // Buscar autor por prefijo
                case 6:
                    System.out.print("Prefijo: ");
                    String pref = scanner.nextLine();

                    System.out.print("Autores: ");
                    System.out.println(
                            String.format(
                                    "ArrayList<String> = {%s}\n",
                                    String.join(", ", collection.consultaAutorPrefijo(pref)))
                    );
                    break;
                // Contenido, dados Título + Autor
                case 7:
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autor: ");
                    String autorc = scanner.nextLine();

                    String content = "~~~~";

                    if (!collection.getIndexAutor().containsKey(autorc)) System.out.println("No existe este autor");
                    else {
                        for (Documento d : collection.getIndexAutor().get(autorc)) {
                            if (d.getTituloString().equals(titulo)) {
                                content = d.getArticuloString();
                                break;
                            }
                        }
                        if (content.equals("~~~~"))
                            System.out.println("No existe un Documento con esta combinación Título-Autor");
                        else System.out.print("Contenido: " + content + "\n");
                    }
                    break;
            }
        }
    }
}

/*package Domain.drivers;

import Domain.Collection;
import Domain.Documento;
import Domain.Palabra;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;

public class DriverCollection {

    public static void run() {
        Collection collection = null;

        try {
            collection = Collection.getInstance();
            System.out.println("Numero total documentos: " + collection.getNDocs());

            SortedMap<String, ArrayList<Documento>> mapaAutores = collection.getIndexAutor();

            ArrayList<Documento> docsJessica = mapaAutores.get("Jessica Mouzo Quintáns");
            for (Documento d : docsJessica) {
                System.out.println("ID: " + d.getId() + ", titulo: " + d.getTituloString());
            }

            SortedMap<Palabra, Double> docsPerWord = collection.getNDocsPerWord();
            for (SortedMap.Entry<Palabra, Double> entry : docsPerWord.entrySet()) {
                System.out. print(
                    entry.getKey() + ": " + entry.getValue() + ". "
                );
            }


        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/
