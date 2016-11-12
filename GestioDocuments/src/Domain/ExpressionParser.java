package Domain;

import com.sun.istack.internal.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

class SyntaxErrorException extends Exception {}

class ParenthesisMismatchException extends SyntaxErrorException {}

public class ExpressionParser {
    private Stack<Token> operatorStack;
    private Queue<Token> outputQueue;

    private ArrayList<Token> tokens;

    public ExpressionParser(@NotNull ArrayList<Token> tokens) {
        operatorStack = new Stack<Token>();
        outputQueue = new ArrayDeque<Token>();
        this.tokens = tokens;
    }

    private Queue<Token> parse() throws SyntaxErrorException {
            for (Token t : tokens) {
                if (t.isWord()) {
                    outputQueue.add(t);
                } else if (t.isWordSet()) {
                    // TODO
                } else if (t.isWordSequence()) {
                    // TODO
                } else if (t.isPrefixOperator()) {
                    operatorStack.add(t);
                } else if (t.isInfixOperator()) {
                    while (!operatorStack.empty() &&
                            t.getPrecedence() <= operatorStack.peek().getPrecedence()) {
                        outputQueue.add(operatorStack.peek());
                        operatorStack.pop();
                    }
                    operatorStack.add(t);
                } else if (t.isLeftParenth()) {
                    operatorStack.add(t);
                } else if (t.isRightParenth()) {
                    while (!operatorStack.empty() && operatorStack.peek().isLeftParenth()) {
                        outputQueue.add(operatorStack.peek());
                        operatorStack.pop();
                    }
                    if (operatorStack.empty())
                        throw new ParenthesisMismatchException();

                    operatorStack.pop();

                    if (operatorStack.empty()) {
                        throw new ParenthesisMismatchException();
                    } else {
                        if (operatorStack.peek().isPrefixOperator()) {
                            outputQueue.add(operatorStack.peek());
                            operatorStack.pop();
                        }
                    }
                }
                else {
                    throw new SyntaxErrorException();
                }
            }

            while (!operatorStack.empty()) {
                Token t = operatorStack.peek();

                if (t.isLeftParenth() || t.isRightParenth())
                    throw new ParenthesisMismatchException();

                outputQueue.add(operatorStack.peek());
                operatorStack.pop();
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

class Token {

    private enum TToken {
        word,
        wordSet,
        wordSequence,
        prefixOperator,
        infixOperator,
        leftPatenth,
        rightParenth
    }

    private String s;
    private final TToken tokenType;

    public Token(@NotNull String s, @NotNull TToken tokenType) {
        this.s = s;
        this.tokenType = tokenType;
    }

    public String toString() {
        return s;
    }

    /**
     * Pre: this.isInfixOperator()
     * @return an integer valid to compare 2 tokens by its precedence
     */
    public int getPrecedence() {
        switch (s) {
            case "!": return 3;
            case "&": return 2;
            case "|": return 1;
            default: return 0; // never reaches default if precondition is true
        }
    }

    public boolean isPrefixOperator() {
        return tokenType == TToken.prefixOperator;
    }

    public boolean isInfixOperator() {
        return tokenType == TToken.infixOperator;
    }

    public boolean isLeftParenth() {
        return tokenType == TToken.leftPatenth;
    }

    public boolean isRightParenth() {
        return tokenType == TToken.rightParenth;
    }

    public boolean isWord() {
        return tokenType == TToken.word;
    }

    public boolean isWordSet() {
        return tokenType == TToken.wordSet;
    }

    public boolean isWordSequence() {
        return tokenType == TToken.wordSequence;
    }
}