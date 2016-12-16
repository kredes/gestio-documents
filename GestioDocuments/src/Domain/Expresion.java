package Domain;

import java.io.IOException;
import java.util.*;

public class Expresion {
    // TODO controlar validació expressió

    public Expresion() {
    }

    public static Set<Documento> validaYEvalua(String expr) throws SyntaxErrorException, IOException {
        Expresion expresion = new Expresion();
        ArrayList<Token> tokens = expresion.generateTokens(expr);
        List<Token> postOrder = expresion.toPostOrder(tokens);
        Node treeRoot = expresion.generateTree(postOrder);
        return expresion.eval(treeRoot);
    }

    public ArrayList<Token> generateTokens(String expr) {
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
        Stack<Token> operatorStack = new Stack<>();
        ArrayList<Token> output = new ArrayList<>();
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

    public Node generateTree(List<Token> postOrder) throws SyntaxErrorException {
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
                    if (i < 2) throw new SyntaxErrorException();

                    Node rChild = nodes.get(i - 1);
                    Node lChild = nodes.get(i - 2);
                    node.setRightChild(rChild);
                    node.setLeftChild(lChild);

                    nodes.remove(i - 1);
                    nodes.remove(i - 2);
                    i -= 2;
                } else { // t.isPrefixOperator()
                    // només 1 de la list
                    if (i < 1) throw new SyntaxErrorException();

                    Node onlyChild = nodes.get(i - 1);
                    node.setLeftChild(onlyChild);

                    nodes.remove(i - 1);
                    --i;
                }
                // post: t.isOperator() i es l'arrel d'un arbre
            }

            ++i;
        }

        if (nodes.size() > 1) {
            throw new SyntaxErrorException();
        }

        return node;
    }

    public Set<Documento> eval(Node tree) throws IOException {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        return evaluator.eval(tree);
    }

    private static class ExpressionEvaluator {
        private Collection collection;
        private int totalDocNum;
        public ExpressionEvaluator() throws IOException {
            collection = Collection.getInstance();
            totalDocNum = collection.getNDocs();
        }

        private static Set<String> parseSet(Token t) {
            Set<String> words; // = new HashSet<>();

            String value = t.toString();

            // Mucho más simple
            words = new HashSet(
                    Arrays.asList(
                            value.substring(1, value.length()-1).split(" ")
                    )
            );

            /*
            int i = 1;
            while (i < value.length()) {
                // Para que no quede en un bucle infinito
                if (value.charAt(i) == ' ') ++i;
                else if (value.charAt(i) == '}') break;

                int inici = i;
                while (i < value.length()-1 && value.charAt(i) != ' ') {
                    ++i;
                }

                String word = value.substring(inici, i);
                words.add(word);
            }*/

            return words;
        }

        public Set<Documento> eval(Node tree) {
            Set<Documento> result;
            Token t = tree.getToken();

            if (t.isWord()) {
                result = collection.queryContainsWordOrSequence(t.toString());
            }
            else if (t.isWordSequence()) {
                String s = t.toString();
                result = collection.queryContainsWordOrSequence(s.substring(1, s.length()-1));
            }
            else if (t.isWordSet()) {
                Set<String> words2Query = parseSet(t);
                result = collection.queryContainsWordSet(words2Query);
            }
            else { // t.isOperator()
                String s = t.toString();
                result = eval(tree.getLeftChild());
                if (s.equals("&")) {
                    if (!result.isEmpty()) {
                        Set<Documento> resultR = eval(tree.getRightChild());
                        // intersection(result,resultR) -> result
                        result.retainAll(resultR);
                    }
                } else if (s.equals("|")) {
                    if (result.size() != totalDocNum) {
                        Set<Documento> resultR = eval(tree.getRightChild());
                        // union(result,resultR) -> result
                        result.addAll(resultR);
                    }
                }
                else { // !
                    Set<Documento> toExclude = result;
                    result = collection.queryAllDocs();
                    result.removeAll(toExclude);
                }
            }

            return result;
        }

    }

}