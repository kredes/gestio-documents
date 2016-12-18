package Domain.tests;

import Domain.Documento;
import Domain.Expresion;
import Domain.ParenthesisMismatchException;
import Domain.SyntaxErrorException;
import org.junit.Test;

import java.io.*;
import java.util.Set;


public class ExpresionTest {

    @Test
    public void validaYEvalua() throws Exception {
        /*
        String[] pruebas = new String[] {
                // docs existentes
                "!sentencia & {patti dylan} & japonés",
                "Chipre & Christy & Estepona & !móvil",
                "!móvil&((((Barcelona))|Valencia))",
                "a",
                "!!a",

                // errores sintaxis
                "",
                "   &, ",
                "!",
                "a!&|b",
                "aniversario noble & canciones",
                "Barcelona!",
                "a!!",

                "a&b!&c",
                "Chipre &",
                "!&((((Barcelona))|Valencia))",
                "{p1 p2 p3} & (\"hola adios\" | pepe & !juan",

                // otros
                "{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  "
        };
        */

        FileInputStream fis = new FileInputStream("joc");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

        String line = reader.readLine();
        while (line != null) {
            Set<Documento> result;
            System.out.println("Búsqueda de: " + line);
            try {
                result = Expresion.validaYEvalua(line);
                System.out.println("Result: " + result.size() + " documentos");
                for (Documento doc : result) {
                    System.out.println("\t" + doc.getId() + ". " + doc.getTituloString());
                }
            }
            catch (ParenthesisMismatchException e) {
                System.out.println("\tError de sintaxis en paréntesis");
            }
            catch (SyntaxErrorException e) {
                System.out.println("\tError de sintaxis");
            }

            System.out.println();

            line = reader.readLine();
        }
    }
}