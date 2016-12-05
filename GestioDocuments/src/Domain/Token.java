package Domain;

public class Token {
    private TToken tokenType;
    private String value;

    // deixa value a null
    public Token(TToken tipus) {
        this.tokenType = tipus;
    }

    public Token(TToken tipus, String value) {
        this.tokenType = tipus;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Pre: this.isInfixOperator()
     * @return an integer valid to compare 2 tokens by its precedence
     */
    public int getPrecedence() {
        switch (value) {
            case "!":
                return 3;
            case "&":
                return 2;
            case "|":
                return 1;
            default:
                return 0; // never reaches default if precondition is true
        }
    }
    public boolean isOperator() {
        return isInfixOperator() || isPrefixOperator();
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
