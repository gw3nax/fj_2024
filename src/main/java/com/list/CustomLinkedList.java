package com.list;

import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class CustomLinkedList<T> implements Iterable<T> {
    private final Node<T> first;
    private final Node<T> last;
    private int size;

    public CustomLinkedList() {
        first = new Node<>(null, null, null);
        last = new Node<>(null, null, null);
        size = 0;
        first.next = last;
        last.prev = first;
    }

    public int size() {
        return size;
    }

    public void add(T newData) {
        Node<T> prev = last.prev;
        Node<T> temp = new Node<>(last, prev, newData);
        prev.next = temp;
        last.prev = temp;
        size++;
    }

    public T get(int index) {
        return getNodeByIndex(index).data;
    }

    private Node<T> getNodeByIndex(int index) {
        if (index >= size || index < 0) throw new IndexOutOfBoundsException("Out of bounds");
        Node<T> temp = first.next;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp;
    }

    public void remove(int index) {
        Node<T> temp = getNodeByIndex(index);
        temp.prev.next = temp.next;
        temp.next.prev = temp.prev;
        size--;
    }

    public boolean contains(T data) {
        Node<T> temp = first;
        while (temp != last) {
            if (temp.data == data) return true;
            temp = temp.next;
        }
        return false;
    }

    public void addAll(CustomLinkedList<? extends T> collection) {
        for (T data : collection) {
            add(data);
        }
    }

    public void addAll(Collection<? extends T> collection) {
        for (T data : collection) {
            add(data);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        Node<T> temp = first.next;
        while (temp != last) {
            builder.append(temp.data);
            if (temp.next != last) builder.append(", ");
            temp = temp.next;
        }
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new CustomIterator();
    }

    @AllArgsConstructor
    public static class Node<T> {
        Node<T> next;
        Node<T> prev;
        T data;
    }

    public class CustomIterator implements Iterator<T> {
        Node<T> current = first.next;

        @Override
        public boolean hasNext() {
            return current != last;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            else {
                T data = current.data;
                current = current.next;
                return data;
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            while (hasNext()) {
                action.accept(next());
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}