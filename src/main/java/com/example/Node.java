package com.example;
public class Node implements Comparable<Node> {
    Byte b;
    int freq ;
    Node left,right;

    public Node(){

    }
    public Node(Byte b, int freq){
        this.b = b;
        this.freq = freq;
        left = null;
        right = null;

    }
    public Node(Byte b, int freq, Node left, Node right) {
        this.b = b;
        this.freq = freq;
        this.left = left;
        this.right = right;
    }
    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.freq, other.freq);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
