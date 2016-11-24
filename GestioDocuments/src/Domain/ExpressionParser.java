package Domain;

import com.sun.istack.internal.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

public class ExpressionParser {
    // TODO controlar validació expressió
    // TODO admetre espais
    private Stack<Token> operatorStack;
    private Queue<Token> outputQueue;

    public ExpressionParser() {
        operatorStack = new Stack<>();
        outputQueue = new ArrayDeque<>();
    }

    public static ArrayList<String> parseSetOrSequence(Token t) {
        ArrayList<String> words = new ArrayList<>();
        int i = 1;
        String value = t.toString();

        while (i < value.length()) {
            int inici = i;
            while (value.charAt(i) != ' ' && i < value.length()) ++i;
            words.add(value.substring(inici, i));

            ++i;
        }
        return words;
    }

    public ArrayList<Token> generaTokens(String expr) {
        ArrayList<Token> tokens = new ArrayList<>();

        int i = 0;
        while (i < expr.length()) {
            Character c = expr.charAt(i);

            if (c == '(') {
                tokens.add(new Token(TToken.leftPatenth, "("));
                ++i;
            } else if (c == ')') {
                tokens.add(new Token(TToken.rightParenth, ")"));
                ++i;
            } else if (c == '!') {
                tokens.add(new Token(TToken.prefixOperator, "!"));
                ++i;
            } else if (c == '|') {
                tokens.add(new Token(TToken.infixOperator, "|"));
                ++i;
            } else if (c == '&') {
                tokens.add(new Token(TToken.infixOperator, "&"));
                ++i;
            } else if (c == '{') {
                int inici = i;
                while (expr.charAt(i) != '}') ++i;
                tokens.add(new Token(TToken.wordSet, expr.substring(inici, i+1)));
                ++i;
            } else if (c == '"') {
                int inici = i;
                ++i;
                while (expr.charAt(i) != '"') ++i;
                tokens.add(new Token(TToken.wordSequence, expr.substring(inici, i+1)));
                ++i;
            } else if (c == ' ') {
                ++i;
            } else { // word
                int inici = i;
                while (i < expr.length() && Character.isLetterOrDigit(expr.charAt(i)))
                    ++i;
                tokens.add(new Token(TToken.wordSequence, expr.substring(inici, i)));
            }
        }

        return tokens;
    }

    public Queue<Token> toRPN(ArrayList<Token> tokens) throws SyntaxErrorException {
            for (Token t : tokens) {
                if (t.isWord()) {
                    outputQueue.add(t);
                } else if (t.isWordSet()) {
                    outputQueue.add(t);
                } else if (t.isWordSequence()) {
                    outputQueue.add(t);
                } else if (t.isPrefixOperator()) {
                    operatorStack.add(t);
                } else if (t.isInfixOperator()) {
                    while (!operatorStack.empty() &&
                            t.getPrecedence() <= operatorStack.peek().getPrecedence()) {
                        outputQueue.add(operatorStack.pop());
                    }
                    operatorStack.add(t);
                } else if (t.isLeftParenth()) {
                    operatorStack.add(t);
                } else if (t.isRightParenth()) {
                    while (!operatorStack.empty() && !operatorStack.peek().isLeftParenth()) {
                        outputQueue.add(operatorStack.pop());
                    }
                    if (operatorStack.empty())
                        throw new ParenthesisMismatchException();

                    operatorStack.pop();

                    if (!operatorStack.empty() && operatorStack.peek().isPrefixOperator()) {
                        outputQueue.add(operatorStack.pop());
                    }
                } else {
                    throw new SyntaxErrorException();
                }
            }

            while (!operatorStack.empty()) {
                Token t = operatorStack.peek();

                if (t.isLeftParenth() || t.isRightParenth())
                    throw new ParenthesisMismatchException();

                outputQueue.add(operatorStack.pop());
            }

        return outputQueue;
    }

    /**
     * pre: t.isWordSet()
     * post: the result contains the words parsed from t
     */
    private ArrayList<String> parseWordSet(@NotNull Token t) {
        // TODO implement method
        return null;
    }

    /**
     * pre: t.isWordSequence()
     * post: the result contains the words parsed from t
     */
    private ArrayList<String> parseWordSequence(@NotNull Token t) {
        // TODO implement method
        return null;
    }
}