package Domain;

import com.sun.istack.internal.NotNull;

import java.util.*;

public class Expresion {
    // TODO controlar validació expressió
    // TODO admetre espais
    private Stack<Token> operatorStack;
    private List<Token> output;

    public Expresion() {
        operatorStack = new Stack<>();
        output = new ArrayList<>();
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

    public List<Token> toPostOrder(List<Token> tokens) throws SyntaxErrorException {
            for (Token t : tokens) {
                if (t.isWord()) {
                    output.add(t);
                } else if (t.isWordSet()) {
                    output.add(t);
                } else if (t.isWordSequence()) {
                    output.add(t);
                } else if (t.isPrefixOperator()) {
                    operatorStack.add(t);
                } else if (t.isInfixOperator()) {
                    while (!operatorStack.empty() &&
                            t.getPrecedence() <= operatorStack.peek().getPrecedence()) {
                        output.add(operatorStack.pop());
                    }
                    operatorStack.add(t);
                } else if (t.isLeftParenth()) {
                    operatorStack.add(t);
                } else if (t.isRightParenth()) {
                    while (!operatorStack.empty() && !operatorStack.peek().isLeftParenth()) {
                        output.add(operatorStack.pop());
                    }
                    if (operatorStack.empty())
                        throw new ParenthesisMismatchException();

                    operatorStack.pop();

                    if (!operatorStack.empty() && operatorStack.peek().isPrefixOperator()) {
                        output.add(operatorStack.pop());
                    }
                } else {
                    throw new SyntaxErrorException();
                }
            }

            while (!operatorStack.empty()) {
                Token t = operatorStack.peek();

                if (t.isLeftParenth() || t.isRightParenth())
                    throw new ParenthesisMismatchException();

                output.add(operatorStack.pop());
            }

        return output;
    }

    public Node generateTree(List<Token> postOrder) {
        List<Node> nodes = new ArrayList<>();
        for (Token t : postOrder) {
            nodes.add(new Node(t));
        }

        // TODO i si no hi ha operadors? o buida?
        int i = 0;
        Node node = null;
        while (i < nodes.size()) {
            node = nodes.get(i);
            if (node.getToken().isOperator()) {
                //break;

                // pre: t.isOperator()
                Token t = node.getToken();
                if (t.isInfixOperator()) {
                    // agafem 2 de la list
                    Node rChild = nodes.get(i - 1);
                    Node lChild = nodes.get(i - 2);
                    node.setRightChild(rChild);
                    node.setLeftChild(lChild);

                    nodes.remove(i - 1);
                    nodes.remove(i - 2);
                    i -= 2;
                } else { // t.isPrefixOperator()
                    // només 1 de la list
                    Node onlyChild = nodes.get(i - 1);
                    node.setLeftChild(onlyChild);

                    nodes.remove(i - 1);
                    --i;
                }
                // post: t.isOperator() i es l'arrel d'un arbre
            }

            ++i;
        }

        // TODO comprovar casos extrems

        return node;
    }


}