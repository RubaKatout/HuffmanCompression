package com.example;


import java.util.ArrayList;

public class MinHeap {
    private ArrayList<Node> heap = new ArrayList<>();

    public int size() {
        return heap.size();
    }

    public void insert(Node node) {
        heap.add(node);
        heapifyInsert(heap.size() - 1);
    }


    public Node extractMin() {
        if (heap.isEmpty()) return null;

        Node min = heap.get(0);
        Node last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDelete(0);
        }
        return min;
    }

    //from last to up
    private void heapifyInsert(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i).compareTo(heap.get(parent)) < 0) {//parent should be less than children
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }
    //from first to down
    private void heapifyDelete(int i) {
        //if parent greater than children , swap between it and the smaller child
        int left, right, smallest;

        while (i < heap.size()) {
            left = 2 * i + 1;
            right = 2 * i + 2;
            smallest = i;

            //if there is left child and is smaller than parent
            if (left < heap.size() && heap.get(left).compareTo(heap.get(smallest)) < 0)
                smallest = left;

            //if there is right child and is smaller than parent
            if (right < heap.size() && heap.get(right).compareTo(heap.get(smallest)) < 0)
                smallest = right;

            if (smallest != i) {
                swap(i, smallest);
                i = smallest;
            } else {
                break;
            }
        }
    }

    private void swap(int i, int j) {
        Node temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}