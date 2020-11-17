import java.util.*;

public class BlumeFilterSet<T extends BlumeElement> implements Set<T> {
    private int capacity;
    private boolean[] array;
    private boolean[] deletedArray;
    private int size;

    BlumeFilterSet(int capacity) {
        this.capacity = capacity;
        array = new boolean[capacity];
        deletedArray = new boolean[capacity];
        size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return array.length > 0;
    }

    public boolean contains(Object o) {
        BlumeElement element = (BlumeElement) o;
        return hashedContains(array, element) && !hashedContains(deletedArray, element);
    }

    private boolean hashedContains(boolean[] array, BlumeElement element) {
        for (boolean value : getElementValues(array, element)) {
            if (!value) return false;
        }
        return true;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }
        };
    }

    public Object[] toArray() {
        return new Object[0];
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    public boolean add(T t) {
        if (t == null) return false;
        setElement(t, true);
        size++;
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) return false;
        setElement((BlumeElement) o, false);
        size--;
        return true;
    }

    private int getIndex(BlumeElement.HashFunction hashFunction){
        return minimize(hashFunction.hash());
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

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }
        return true;
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T el : c) {
            add(el);
        }
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public boolean removeAll(Collection<?> c) {
        for (Object o : c) {
            remove(o);
        }
        return true;
    }

    public void clear() {
        array = new boolean[capacity];
        deletedArray = new boolean[capacity];
    }

    private int minimize(int hash) {
        hash = Math.abs(hash);
        while (hash > capacity) {
            hash /= 10;
        }
        return hash;
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
