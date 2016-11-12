package Domain.drivers;

import Domain.Documento;
import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

public class DriverPersistencia {

    private static ControladorPersistencia ctrlPersistencia;
    private static Scanner scanner;

    public DriverPersistencia() {
        ctrlPersistencia = ControladorPersistencia.getInstance();
        scanner = new Scanner(System.in);
    }

    public static void run() throws IOException {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    "Testeando el controlador de la capa de persistencia (clase ControladorPersistencia).\n" +
                            "   1. Ver estado interno\n" +
                            "   2. Retornar todas las IDs de documentos (archivo 'meta/ids')\n" +
                            "   3. Retornar documento por ID\n" +
                            "   4. Guardar documento\n" +
                            "   5. Modificar documento\n" +
                            "   6. Eliminar documento\n" +
                            "   0. Volver\n"
            );

            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Volver
                case 0:
                    return;
                // Ver estado interno
                case 1: {
                    int idCounter = ctrlPersistencia.getIdCounter();
                    Path FOLDER = ctrlPersistencia.getFolder();
                    Path METAFOLDER = ctrlPersistencia.getMetaFolder();
                    System.out.println(
                            String.format(
                                    "int idCounter = %d  (éste será el ID del siguiente documento insertado)\n" +
                                    "Guardando documentos en '%s'\n" +
                                    "Guardando metadatos en '%s'\n",
                                    idCounter, FOLDER.toString(), METAFOLDER.toString())
                    );
                    break;
                }
                // Retornar todas las IDs de documentos
                case 2: {
                    ArrayList<String> ids = new ArrayList<>();
                    for (Integer id : ctrlPersistencia.getDocIDs()) {
                        ids.add(String.valueOf(id));
                    }
                    System.out.println(
                            String.format(
                                    "docIDs = {%s}\n",
                                    String.join(", ", ids))
                    );
                    break;
                }
                // Retornar documento por ID
                case 3: {
                    System.out.print(
                            "Introduce ID (o vuelve atrás y pulsa 2 para ver todas las IDs válidas): "
                    );
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Documento d;
                    try {
                        d = ctrlPersistencia.getDocumento(id);
                        System.out.println(
                                String.format(
                                        "Título: %s\n" +
                                                "Autores: %s\n" +
                                                "Categoría: %s\n" +
                                                "Contenido:\n" +
                                                "%s\n",
                                        d.getTituloString(), String.join(", ", d.getAutoresStrings()),
                                        d.getEtiquetasStrings().get(0), d.getArticuloString())
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Guardar documento
                case 4: {
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
                        ctrlPersistencia.guardaDocumento(d, 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Modificar documento
                case 5: {
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
                        ctrlPersistencia.sobreescribirDocumento(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                // Eliminar documento
                case 6:
                    System.out.print("Introduce la ID del documento a eliminar: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    try {
                        Documento d = ctrlPersistencia.getDocumento(id);
                        ctrlPersistencia.eliminarDocumento(d);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }
}
