package Domain.drivers;

import Domain.Contenido;
import Domain.Documento;
import Domain.Frase;
import Domain.Palabra;
import Persistence.ControladorPersistencia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class DriverDocumentoContenidoFrasePalabra {

    private static ControladorPersistencia ctrlPersistencia;
    private static Scanner scanner;

    public DriverDocumentoContenidoFrasePalabra() {
        ctrlPersistencia = ControladorPersistencia.getInstance();
        scanner = new Scanner(System.in);
    }



    public static void run() throws IOException {
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            System.out.println(
                    "Testeando clases: (Documento, Contenido, Frase, Palabra).\n" +
                            "   1. Crear Documento (Contenido, Frases y palabras) y mostrar: \n" +
                            "   2. Crear contenido y retornarlo: \n" +
                            "   3. Crear frase y retornarla: \n" +
                            "   4. Crear palabra y retornarla: \n" +
                            "   0. Volver"
            );

            int seleccion = scanner.nextInt();
            scanner.nextLine();
            switch (seleccion) {
                // Volver
                case 0:
                    return;
                // Crear Documento (Contenido, Frases y palabras)
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

                    System.out.println("Contenido (terminado por la cadena '~~'): ");
                    StringBuilder contenido = new StringBuilder();
                    String aux;
                    while (!(aux = scanner.nextLine()).equals("~~")) {
                        contenido.append(aux + "\n");
                    }

                    Documento d = new Documento(ctrlPersistencia.nextId(), titulo, autores, tags, contenido.toString());

                    System.out.println(
                            String.format(
                                    "Título: %s\n" +
                                            "Autores: %s\n" +
                                            "Categoría: %s\n" +
                                            "Contenido:\n" +
                                            "%s\n" + "Palabras válidas del documento: %s\n",
                                    d.getTituloString(), String.join(", ", d.getAutoresStrings()),
                                    d.getEtiquetasStrings().get(0), d.getArticuloString(), d.getNumeroPalabras()-1)
                    );

                    break;
                }
                // Crear contenido y retornarlo
                case 2: {
                    System.out.println("Escribe contenido (terminado por la cadena '~~'): ");
                    StringBuilder contenido = new StringBuilder();
                    String aux;
                    while (!(aux = scanner.nextLine()).equals("~~")) {
                        contenido.append(aux + "\n");
                    }

                    Contenido contenido1 = new Contenido(contenido.toString());
                    System.out.println("Frases del contenido: " + contenido1.getFrasesLength());
                    System.out.println("Palabras del contenido (válidas, sin stockwords): " + contenido1.getWordsLength());


                    break;
                }
                // Crear frase y retornala
                case 3: {
                    System.out.println("Escribe una frase (terminada por un punto): ");
                    StringBuilder frase = new StringBuilder();
                    String aux;
                    aux = scanner.nextLine();
                    frase.append(aux);

                    Frase f = new Frase(frase.toString());
                    System.out.println("Número de palabras de la frase (válidas, sin stock words): " + f.getLength());

                    System.out.println("Frase entrada: " + frase.toString());
                    System.out.println("Frase sin stock words: " + f.getFrase());
                    System.out.println("Palabras de la frase sin stock words: " + f.getLength());
                    System.out.println("Primera palabra de la frase: " + f.getWord(0) + "\n");

                    break;
                }
                // Crear palabra y retornarla
                case 4: {
                    System.out.println("Escribe dos palabras (una por línea): ");
                    Palabra palabra1 = new Palabra(scanner.nextLine());
                    Palabra palabra2 = new Palabra(scanner.nextLine());

                    System.out.println("Primera palabra: " + palabra1.toString());
                    System.out.println("Segunda palabra: " + palabra2.toString() + "\n");
                    System.out.println("Son iguales: ");
                    if(palabra1.compareTo(palabra2) == 0) System.out.println("SÍ");
                    else System.out.println("NO");
                    System.out.println("Substituye palabra 1 por otra: ");
                    palabra1.setPalabra(scanner.nextLine());
                    System.out.println("Substituye palabra 2 por otra: ");
                    palabra2.setPalabra(scanner.nextLine());
                    System.out.println("Primera palabra: " + palabra1.toString());
                    System.out.println("Segunda palabra: " + palabra2.toString() + "\n");


                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DriverDocumentoContenidoFrasePalabra driver = new DriverDocumentoContenidoFrasePalabra();
        driver.run();
    }

}
