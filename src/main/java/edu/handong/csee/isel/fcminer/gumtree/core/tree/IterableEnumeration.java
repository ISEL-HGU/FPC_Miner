package edu.handong.csee.isel.fcminer.gumtree.core.tree;

import java.util.Enumeration;
import java.util.Iterator;

public abstract class IterableEnumeration<T> implements Iterable<T> {

    public static <T> Iterable<T> make(Enumeration<T> en) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return en.hasMoreElements();
                    }

                    @Override
                    public T next() {
                        return en.nextElement();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
