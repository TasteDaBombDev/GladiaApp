package com.zone.app.utils;

import java.util.ArrayList;
import java.util.Stack;
import java.util.function.BiFunction;


public class AVLtree<V> {

    private Node<V> root;
    private BiFunction<V,V,Integer> sort;


    public AVLtree(BiFunction<V, V, Integer> sort) {
        this.sort = sort;
        root = null;
    }

    private void setRoot(Node<V> node) {
        root = node;
    }

    public Node<V> getTree() {
        return root;
    }

    public V searchForVal(V val) {
        return searchVal(root, val);
    }

    public boolean searchExistance(V val) {
        return search(root, val);
    }


    private boolean search(Node<V> node, V val) {
        if (node == null) {
            return false;
        }
        if (sort.apply(val, node.getVal()) < 0)
            return search(node.getLeft(), val);
        else if (sort.apply(val, node.getVal()) > 0)
            return search(node.getRight(), val);
        else
            return true;
    }

    private V searchVal(Node<V> node, V val) {
        if (node == null) {
            return null;
        }
        if (sort.apply(val, node.getVal()) < 0)
            return searchVal(node.getLeft(), val);
        else if (sort.apply(val, node.getVal()) > 0)
            return searchVal(node.getRight(), val);
        else
            return node.getVal();
    }

    public void removeVal(V val) {
        root = remove(root, val);
    }

    public void addVal(V val) {
        root = add(root, val);
    }

    private Node<V> add(Node<V> node, V val) {
        if (node == null) {
            return new Node<V>(val);
        }

        //-----------[BTS INSERATION]--------------
        if (sort.apply(val, node.getVal()) < 0)
            node.setLeft(add(node.getLeft(), val));// Search in left node
        else if (sort.apply(val, node.getVal()) > 0)
            node.setRight(add(node.getRight(), val));// Search in right node
        else
            return node;



        //-----------------[RECALCULATE_HEIGHT]---------------
        node.setHeight(1 + Math.max(height(node.getLeft()), height(node.getRight())));

        int balance = calcBalance(node);



        //----------------[REBALANCE_TREE]----------------------

        // CASE LEFT LEFT
        if (balance > 1 && sort.apply(val, node.getLeft().getVal()) < 0)
            return toRightRotation(node);

        // CASE RIGHT RIGHT
        if (balance < -1 && sort.apply(val, node.getRight().getVal()) > 0)
            return toLeftRotation(node);

        // CASE LEFT RIGHT
        if (balance > 1 && sort.apply(val, node.getLeft().getVal()) > 0) {
            node.setLeft(toLeftRotation(node.getLeft()));
            return toRightRotation(node);
        }

        // CASE RIGHT LEFT
        if (balance < -1 && sort.apply(val, node.getRight().getVal()) < 0) {
            node.setRight(toRightRotation(node.getRight()));
            return toLeftRotation(node);
        }

        return node;
    }

    private Node<V> remove(Node<V> node, V val) {
        if (node == null)
            return node;
        //-----------[BTS deletion]--------------
        if (sort.apply(val, node.getVal()) < 0) {
            node.setLeft(remove(node.getLeft(), val));
        } else if (sort.apply(val, node.getVal()) > 0) {
            node.setRight(remove(node.getRight(), val));
        } else {
            //------------[FOUNDED_NODE]----------------
            if ((node.getLeft() == null) || (node.getRight() == null)) {
                Node<V> T = null;
                if (node.getLeft() == null)
                    T = node.getRight();
                else
                    T = node.getLeft();

                if (T == null) {
                    T = node;
                    node = null;
                } else
                    node = T;
            } else {
                Node<V> T = node.getRight();

                while (T.getLeft() != null)
                    T = T.getLeft();

                node.setVal(T.getVal());

                node.setRight(remove(node.getRight(), T.getVal()));
            }
        }


        if (node == null) {
            return node;
        }

        //-----------------[RECALCULATE_HEIGHT]---------------
        node.setHeight(calcHeight(node));

        int balance = calcBalance(node);


        //----------------[REBALANCE_TREE]----------------------
        // CASE LEFT LEFT
        if (balance > 1 && calcBalance(node.getLeft()) >= 0)
            return toRightRotation(node);

        // CASE RIGHT RIGHT
        if (balance < -1 && calcBalance(node.getRight()) <= 0)
            return toLeftRotation(node);

        // CASE LEFT RIGHT
        if (balance > 1 && calcBalance(node.getLeft()) < 0) {
            node.setLeft(toLeftRotation(node.getLeft()));
            return toRightRotation(node);
        }

        // CASE RIGHT LEFT
        if (balance < -1 && calcBalance(node.getRight()) > 0) {
            node.setRight(toRightRotation(node.getRight()));
            return toLeftRotation(node);
        }

        return node;
    }

    public ArrayList<V> toArrayList() {
        return toArrayList(root);
    }

    private ArrayList<V> toArrayList(Node<V> iterator) {
        ArrayList<V> list = new ArrayList<>();

        Stack<Node<V>> s = new Stack<>();

        while (iterator != null || s.size() > 0) {

            while (iterator != null) {
                s.push(iterator);
                iterator = iterator.getLeft();
            }

            iterator = s.pop();

            list.add(iterator.getVal());

            iterator = iterator.getRight();
        }

        return list;
    }

    public int size() {
        return sizeOfTree(root);
    }

    private int sizeOfTree(Node<V> iterator) {
        int i = 0;

        Stack<Node<V>> s = new Stack<>();

        while (iterator != null || s.size() > 0) {

            while (iterator != null) {
                s.push(iterator);
                iterator = iterator.getLeft();
            }

            iterator = s.pop();

            i++;

            iterator = iterator.getRight();
        }

        return i;
    }

    private int height(Node<V> node) {
        if (node == null)
            return 0;
        return node.getHeight();
    }

    private Node<V> toRightRotation(Node<V> yNode) {
        // Rotate
        Node<V> xNode = yNode.getLeft();
        Node<V> T = xNode.getRight();

        xNode.setRight(yNode);
        yNode.setLeft(T);

        // Update height
        yNode.setHeight(Math.max(height(yNode.getLeft()), height(yNode.getRight())) + 1);
        xNode.setHeight(Math.max(height(xNode.getLeft()), height(xNode.getRight())) + 1);

        return xNode;
    }

    private Node<V> toLeftRotation(Node<V> yNode) {

        // Rotate
        Node<V> xNode = yNode.getRight();
        Node<V> T = xNode.getLeft();

        xNode.setLeft(yNode);
        yNode.setRight(T);

        // Update getHeight()s
        yNode.setHeight(Math.max(height(yNode.getLeft()), height(yNode.getRight())) + 1);
        xNode.setHeight(Math.max(height(xNode.getLeft()), height(xNode.getRight())) + 1);

        return xNode;
    }


    private int calcBalance(Node<V> N) {
        if (N == null)
            return 0;
        return height(N.getLeft()) - height(N.getRight());
    }


    private int calcHeight(Node<V> node) {
        return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
    }


    private Node<V> minValueNode(Node<V> node) {
        Node<V> current = node;
        while (current.getLeft() != null)
            current = current.getLeft();

        return current;
    }

}