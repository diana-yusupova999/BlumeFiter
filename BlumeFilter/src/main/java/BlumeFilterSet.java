import java.util.*;

import static java.lang.Math.*;

public class BlumeFilterSet<T extends BlumeElement> extends AbstractSet<T> {
    private final int capacity;
    private int size;
    private boolean[] array;
    private boolean[] deletedArray;
    private BlumeElement[] elements;

    BlumeFilterSet(int capacity) {
        this.capacity = capacity;
        array = new boolean[this.capacity];
        deletedArray = new boolean[this.capacity];
        elements = new BlumeElement[capacity];
        size = 0;
    }

    BlumeFilterSet(int maxMembers, double errorProbability) {
        this(optimalCapacity(maxMembers, errorProbability));
    }

    BlumeFilterSet(Collection<T> collection, int capacity) {
        this(capacity);
        addAll(collection);
    }

    private static int optimalCapacity(int maxMembers, double errorProbability) {
        return (int) -(maxMembers * log(errorProbability) / pow(log(2.0), 2));
    }

    public boolean add(T t) {
        if (t == null) return false;
        if (size < capacity) elements[size] = t;
        setElement(t, true);
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) return false;
        setElement((BlumeElement) o, false);
        if (size > 0) size--;
        return true;
    }

    public boolean contains(Object o) {
        BlumeElement element = (BlumeElement) o;
        return hashedContains(array, element) && !hashedContains(deletedArray, element);
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T el : c) {
            add(el);
        }
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    private boolean hashedContains(boolean[] array, BlumeElement element) {
        for (boolean value : getElementValues(array, element)) {
            if (!value) return false;
        }
        return true;
    }

    private int getIndex(BlumeElement.HashFunction hashFunction){
        return optimize(hashFunction.hash());
    }

    private int optimize(int hash) {
        hash = Math.abs(hash);
        return hash%capacity;
    }

    private boolean[] getElementValues(boolean[] array, BlumeElement element){
        BlumeElement.HashFunction[] nulls = new BlumeElement.HashFunction[]{()->0};
        BlumeElement.HashFunction[] hashFunctions = element == null ? nulls : element.hash();
        boolean[] values = new boolean[hashFunctions.length];
        int i = 0;
        for (BlumeElement.HashFunction hashFunction : hashFunctions){
            values[i] = array[getIndex(hashFunction)];
            i++;
        }
        return values;
    }

    private void setElement(BlumeElement element, boolean value){
        for (BlumeElement.HashFunction hashFunction : element.hash()) {
            array[getIndex(hashFunction)] = value;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public T next() {
                BlumeElement e = elements[i];
                i++;
                return (T)e;
            }
        };
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        array = new boolean[capacity];
        deletedArray = new boolean[capacity];
        elements = new BlumeElement[capacity];
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlumeFilterSet<?> that = (BlumeFilterSet<?>) o;
        return capacity == that.capacity &&
                size == that.size &&
                Arrays.equals(array, that.array) &&
                Arrays.equals(deletedArray, that.deletedArray);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(capacity, size);
        result = 31 * result + Arrays.hashCode(array);
        result = 31 * result + Arrays.hashCode(deletedArray);
        return result;
    }

    @Override
    public String toString() {
        return "BlumeFilterSet{" +
                ", array=" + Arrays.toString(array) +
                ", deletedArray=" + Arrays.toString(deletedArray) +
                '}';
    }

}
