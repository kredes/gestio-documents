package Domain;


import com.sun.istack.internal.NotNull;

public class Node {
    private Token token;
    private Node leftChild;
    private Node rightChild;

    public Node(@NotNull Token token) {
        this.token = token;
    }

    public Token getToken() {
        return token;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }
}