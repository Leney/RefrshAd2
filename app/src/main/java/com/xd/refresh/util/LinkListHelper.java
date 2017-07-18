package com.xd.refresh.util;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Karma on 2017/7/18.
 */

public class LinkListHelper<E> {


    LinkedList<E> list;

    public LinkListHelper() {
//		list = (LinkedList<E>) Collections.synchronizedList(new LinkedList<>());
        list = new LinkedList<>();
    }

    public E getFirst() {
        synchronized (list) {
            return list.getFirst();
        }
    }

    public E getLast() {
        synchronized (list) {
            return list.getLast();
        }
    }

    public E get(int index) {
        synchronized (list) {
            E e = list.get(index);
            return e;
        }
    }

    public void add(E e) {
        synchronized (list) {
            list.add(e);
        }
    }

    public void add(int indext, E e) {
        synchronized (list) {
            list.add(indext, e);
        }
    }

    public void addAll(Collection<E> arg0) {
        synchronized (list) {
            list.addAll(arg0);
        }
    }

    public void addAll(int index, Collection<E> arg0) {
        synchronized (list) {
            list.addAll(index, arg0);
        }
    }

    public void remove(int index) {
        synchronized (list) {
            if(list.isEmpty()){
                return;
            }
            list.remove(index);
        }
    }

    public void remove(E e) {
        synchronized (list) {
            if(list.isEmpty()){
                return;
            }
            list.remove(e);
        }
    }

    public void removeFirst() {
        synchronized (list) {
            if(list.isEmpty()){
                return;
            }
            list.removeFirst();
        }
    }

    public void removeLast() {
        synchronized (list) {
            if(list.isEmpty()){
                return;
            }
            list.removeLast();
        }
    }

    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    public boolean isEmpty() {
        synchronized (list) {
            return list.isEmpty();
        }
    }

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }


}
