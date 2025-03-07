/*
Manav Malik - mmalik37@uic.edu - 654488575
project 1 - implimentation of a dictnery which can have elements of
any type <k,v> , using 2 methods hashtable and binary serch tree and testing the functionality
*/


import java.util.Iterator;

public class HashMapDict<K,V> implements ProjOneDictionary<K,V> {

    private LinkedListDictionary<K,V>[] bucket;
    private int bucket_size;
    private int size;
    private static final double LOAD_F = 1;

    public HashMapDict(){
        this.bucket = new LinkedListDictionary[10];
        this.bucket_size = 10;
        this.size = 0;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % bucket.length);
    }

    private void resize() {
        int newBucketSize = bucket_size * 2;
        LinkedListDictionary<K, V>[] oldBucket = bucket;

        bucket = new LinkedListDictionary[newBucketSize];
        bucket_size = newBucketSize;
        size = 0;

        for (LinkedListDictionary<K, V> list : oldBucket) {
            if (list != null) {
                for (K key : list) {
                    try {
                        insert(key, list.find(key));
                    } catch (NullValueException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    @Override
    public boolean insert(K key, V value) throws NullValueException {
        if (value == null) throw new NullValueException();
        if (key == null) throw new NullValueException();

        int index = getBucketIndex(key);
        if (bucket[index] == null) {
            bucket[index] = new LinkedListDictionary<>();
        }

        boolean isNewKey = bucket[index].find(key) == null;
        if (bucket[index].insert(key, value)) {
            if (isNewKey) size++;
        }

        if ((double) size / bucket_size > LOAD_F) {
            resize();
        }

        return true;
    }

    @Override
    public V find(K key) {
        int index = getBucketIndex(key);
        if (bucket[index] != null) {
            return bucket[index].find(key);
        }
        return null;
    }

    @Override
    public boolean delete(K key) {
        int index = getBucketIndex(key);
        if (bucket[index] != null && bucket[index].delete(key)) {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Iterator<K> iterator() {
        return new Iterator<K>() {
            private int bucketIndex = 0;
            private Iterator<K> currentIterator = getNextIterator();

            private Iterator<K> getNextIterator() {
                while (bucketIndex < bucket_size && (bucket[bucketIndex] == null || !bucket[bucketIndex].iterator().hasNext())) {
                    bucketIndex++;
                }
                if (bucketIndex < bucket_size) {
                    return bucket[bucketIndex].iterator();
                }
                return null;
            }

            @Override
            public boolean hasNext() {
                return currentIterator != null && currentIterator.hasNext();
            }

            @Override
            public K next() {
                K key = currentIterator.next();
                if (!currentIterator.hasNext()) {
                    bucketIndex++;
                    currentIterator = getNextIterator();
                }
                return key;
            }
        };
    }
}
