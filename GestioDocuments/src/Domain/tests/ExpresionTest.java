package Domain.tests;

import Domain.Documento;
import Domain.Expresion;
import Domain.ParenthesisMismatchException;
import Domain.SyntaxErrorException;
import Domain.drivers.DriverExpresion;
import org.junit.Test;

import java.util.Set;


public class ExpresionTest {

    @Test
    public void validaYEvalua() throws Exception {
        String[] pruebas = new String[] {
                // docs existentes
                "aniversario noble & terrorista",
                "!sentencia & {patti dylan} & japonés",
                "Chipre & Christy & Estepona & !móvil",
                "!móvil&((((Barcelona))|Valencia))",

                // errores sintaxis
                "",
                "   &, ",
                "a!&|b",
                "a!", // TODO fix
                "a!!",
                "!!a",
                "a&b!&c",
                "Chipre &",
                "!&((((Barcelona))|Valencia))",
                "{p1 p2 p3} & (\"hola adios\" | pepe & !juan",

                // otros
                "{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  "
        };

        for (String s : pruebas) {
            Set<Documento> result;
            System.out.println("Búsqueda de: " + s);
            try {
                result = Expresion.validaYEvalua(s);
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
        }
    }
}