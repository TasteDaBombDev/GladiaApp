package com.zone.app.utils;

public class Node<V> {

    private V val;
    private int height;
    private Node<V> left, right;

    public Node(V val) {
        this.val = val;
        this.height = 1;
        this.left = null;
        this.right = null;
    }

    public V getVal(){
        return val;
    }

    public void setVal(V val){
        this.val = val;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int h){
        this.height = h;
    }

    public Node<V> getLeft(){
        return left;
    }

    public Node<V> getRight(){
        return right;
    }

    public void setLeft(Node<V> node){
        this.left = node;
    }

    public void setRight(Node<V> node){
        this.right = node;
    }
}