package Domain.drivers;

import Domain.Collection;
import Domain.Documento;
import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
                    "Testeando la clase Collection de la capa de dominio (clase Collection).\n" +
                            "   1. Añadir documento\n" +
                            "   2. Modificar documento\n" +
                            "   3. Eliminar documento\n" +
                            "   0. Volver\n"
            );

            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Volver
                case 0:
                    return;
                //Añadir documento
                case 1: {
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

                    System.out.println("Contenido (terminado por una línea con sólamente la cadena '~~'): ");
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
                case 2: {
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

                    System.out.println("Nuevo contenido (terminado por una línea con sólamente la cadena '~~'): ");
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
                case 3:
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