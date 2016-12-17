package Domain.drivers;

import Domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DriverExpresion {

    /*
    private static final String DUMMY = "!sentencia & {patti dylan} & japonés";

    private static String tokenArrayToString(List<Token> tokens) {
        ArrayList<String> tokenStringList = new ArrayList<>();
        for (Token t : tokens) tokenStringList.add(t.toString());
        return String.join(", ", tokenStringList);
    }
    */

    public static void run() throws SyntaxErrorException, IOException {
        System.out.println("Empezamos");
        Collection.getInstance();

        //ArrayList<Token> tokens = parser.generaTokens("{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  ");
        //ArrayList<Token> tokens = expresion.generateTokens("{p1 p2 p3}&(\"hola adios\"|!((a|b)&c))&!juan");
/*
        System.out.println(String.format("Testeando Expresion: \"%s\"\n", DUMMY));

        System.out.println("Generando tokens...");
        ArrayList<Token> tokens = expresion.generateTokens(DUMMY);
        System.out.print(String.format("Tokens: %s\n", tokenArrayToString(tokens)));

        List<Token> postOrder = expresion.toPostOrder(tokens);
        System.out.println(String.format("Tokens en postorden: %s\n", tokenArrayToString(postOrder)));

        Node treeRoot = expresion.generateTree(postOrder);

        System.out.println("Buscando documentos que cumplan la expresión...");
        Set<Documento> result = expresion.eval(treeRoot);
        for (Documento d : result) System.out.println(String.format("\t%d. %s", d.getId(), d.getTituloString()));
*/

    }
}
