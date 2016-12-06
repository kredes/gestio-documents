package Domain.drivers;

import Domain.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DriverExpresion {

    public static void run() throws SyntaxErrorException, IOException {
        Expresion expresion = new Expresion();
        //ArrayList<Token> tokens = parser.generaTokens("{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  ");
        //ArrayList<Token> tokens = expresion.generateTokens("{p1 p2 p3}&(\"hola adios\"|!((a|b)&c))&!juan");
        ArrayList<Token> tokens = expresion.generateTokens("!sentencia & {patti dylan} & japonés");

        List<Token> postOrder = expresion.toPostOrder(tokens);

        Node treeRoot = expresion.generateTree(postOrder);

        Set<Documento> result = expresion.eval(treeRoot);
    }

}
