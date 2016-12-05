package Domain.drivers;

import Domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DriverExpresion {

    public static void run() throws SyntaxErrorException {
        Expresion expresion = new Expresion();
        //ArrayList<Token> tokens = parser.generaTokens("{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  ");
        ArrayList<Token> tokens = expresion.generaTokens("{p1 p2 p3}&(\"hola adios\"|!((a|b)&c))&!juan");

        List<Token> postOrder = expresion.toPostOrder(tokens);

        Node treeRoot = expresion.generateTree(postOrder);
    }

}
