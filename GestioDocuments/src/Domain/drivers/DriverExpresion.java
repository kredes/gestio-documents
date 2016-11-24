package Domain.drivers;

import Domain.ExpressionParser;
import Domain.SyntaxErrorException;
import Domain.Token;

import java.util.ArrayList;
import java.util.Queue;

public class DriverExpresion {

    public static void run() throws SyntaxErrorException {
        ExpressionParser parser = new ExpressionParser();
        //ArrayList<Token> tokens = parser.generaTokens("{p1 p2 p3}   & (\"hola adios\" |pepe )  &     ! juan  ");
        ArrayList<Token> tokens = parser.generaTokens("{p1 p2 p3}&(\"hola adios\"|!((a|b)&c))&!juan");

        Queue<Token> rpnTokens = parser.toRPN(tokens);
    }

}
