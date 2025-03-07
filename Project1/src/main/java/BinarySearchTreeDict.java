/*
Manav Malik - mmalik37@uic.edu - 654488575
project 1 - implimentation of a dictnery which can have elements of
any type <k,v> , using 2 methods hashtable and binary serch tree and testing the functionality
*/

import java.util.Iterator;

public class BinarySearchTreeDict<K extends Comparable<K>,V> implements ProjOneDictionary<K,V> {

    private class Node<K extends Comparable<K>,V> {
        K key;
        V value;
        Node left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.right = null;
            this.left = null;
        }
    }

    private Node root;
    private int size = 0;


    @Override
    public boolean insert(K key, V value) throws NullValueException {
        if (value == null || key == null) {
            throw new NullValueException();
        }
        if (root == null) {
            root = new Node(key, value);
            size++;
            return true;
        }
        return insertRecursive(root, key, value);
    }

    private boolean insertRecursive(Node current, K key, V value) {
        int cmp = key.compareTo((K) current.key);
        if (cmp == 0) {
            current.value = value;
            return true;
        } else if (cmp < 0) {
            if (current.left == null) {
                current.left = new Node(key, value);
                size++;
                return true;
            } else {
                return insertRecursive(current.left, key, value);
            }
        } else {
            if (current.right == null) {
                current.right = new Node(key, value);
                size++;
                return true;
            } else {
                return insertRecursive(current.right, key, value);
            }
        }
    }

    @Override
    public V find(K key) {
        return findRecursive(root, key);
    }

    private V findRecursive(Node current, K key) {
        if (current == null) return null;
        int cmp = key.compareTo((K) current.key);
        if (cmp == 0) return (V) current.value;
        if (cmp < 0) return (V) findRecursive(current.left, key);
        return (V) findRecursive(current.right, key);
    }


    @Override
    public boolean delete(K key) {
        if (find(key) == null) return false;
        root = deleteRecursive(root, key);
        size--;
        return true;
    }

    @Override
    public int getSize() {
        return size;
    }

    private Node deleteRecursive(Node current, K key) {
        if (current == null) return null;
        int cmp = key.compareTo((K) current.key);
        if (cmp < 0) {
            current.left = deleteRecursive(current.left, key);
        } else if (cmp > 0) {
            current.right = deleteRecursive(current.right, key);
        } else {
            if (current.left == null) return current.right;
            if (current.right == null) return current.left;
            Node successor = findMin(current.right);
            current.key = successor.key;
            current.value = successor.value;
            current.right = deleteRecursive(current.right, (K) successor.key);
        }
        return current;
    }

    private Node findMin(Node current) {

        while (current!=null &&  current.left != null) current = current.left;
        return current;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private Node current = findMin(root);

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public K next() {
                K key = (K) current.key;
                current = findSuccessor(current);
                return key;
            }
        };
    }

    private Node<K,V> findSuccessor(Node<K,V> node) {
        if (node.right != null) return findMin(node.right);
        Node successor = null;
        Node ancestor = root;
        while (ancestor != node) {
            if (node.key.compareTo((K) ancestor.key) < 0) {
                successor = ancestor;
                ancestor = ancestor.left;
            } else {
                ancestor = ancestor.right;
            }
        }
        return successor;
    }
}