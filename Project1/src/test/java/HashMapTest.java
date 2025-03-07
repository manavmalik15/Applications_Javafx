/*
Manav Malik - mmalik37@uic.edu - 654488575
project 1 - implimentation of a dictnery which can have elements of
any type <k,v> , using 2 methods hashtable and binary serch tree and testing the functionality
*/

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public class HashMapTest extends DictionaryTest {
    @Override
    public ProjOneDictionary<String, String> newDictionary() {
        return new HashMapDict<String, String>();
    }

    private ProjOneDictionary<String, String> dictionary;

    @BeforeEach
    public void setUp() {
        dictionary = newDictionary();
    }

    // Empty case tests
    @Test
    public void testEmptyFind() {
        assertNull(dictionary.find("key1"), "Find on empty dictionary should return null");
    }

    @Test
    public void testEmptyDelete() {
        assertFalse(dictionary.delete("key1"), "Delete on empty dictionary should return false");
    }

    @Test
    public void testEmptySize() {
        assertEquals(0, dictionary.getSize(), "Size of empty dictionary should be zero");
    }

    @Test
    public void testEmptyIterator() {
        Iterator<String> iterator = dictionary.iterator();
        assertFalse(iterator.hasNext(), "Iterator on empty dictionary should not have next");
    }

    // Single entry tests
    @Test
    public void testSingleInsertAndFind() throws NullValueException {
        dictionary.insert("key1", "value1");
        assertEquals("value1", dictionary.find("key1"), "Failed to find inserted value");
    }

    @Test
    public void testSingleDelete() throws NullValueException {
        dictionary.insert("key1", "value1");
        assertTrue(dictionary.delete("key1"), "Failed to delete single key");
        assertNull(dictionary.find("key1"), "Deleted key should not be found");
    }

    @Test
    public void testSingleSize() throws NullValueException {
        dictionary.insert("key1", "value1");
        assertEquals(1, dictionary.getSize(), "Size should be 1 after single insertion");
        dictionary.delete("key1");
        assertEquals(0, dictionary.getSize(), "Size should be 0 after deletion");
    }

    // Multiple entries tests
    @Test
    public void testMultipleInsertsAndFind() throws NullValueException {
        for (int i = 1; i <= 10; i++) {
            dictionary.insert("key" + i, "value" + i);
        }
        for (int i = 1; i <= 10; i++) {
            assertEquals("value" + i, dictionary.find("key" + i), "Failed to find value for key" + i);
        }
    }

    @Test
    public void testMultipleDeletes() throws NullValueException {
        for (int i = 1; i <= 5; i++) {
            dictionary.insert("key" + i, "value" + i);
        }
        for (int i = 1; i <= 5; i++) {
            assertTrue(dictionary.delete("key" + i), "Failed to delete key" + i);
            assertNull(dictionary.find("key" + i), "Deleted key should not be found" + i);
        }
    }

    @Test
    public void testMultipleSize() throws NullValueException {
        for (int i = 1; i <= 10; i++) {
            dictionary.insert("key" + i, "value" + i);
        }
        assertEquals(10, dictionary.getSize(), "Size mismatch after multiple insertions");
    }

    // Edge cases
    @Test
    public void testNullKey() {
        assertThrows(NullValueException.class, () -> dictionary.insert(null, "value"), "Inserting null key should throw exception");
    }

    @Test
    public void testNullValue() {
        assertThrows(NullValueException.class, () -> dictionary.insert("key1", null), "Inserting null value should throw exception");
    }

    @Test
    public void testDuplicateKeys() throws NullValueException {
        dictionary.insert("key1", "value1");
        dictionary.insert("key1", "value2");
        assertEquals("value2", dictionary.find("key1"), "Failed to overwrite value for duplicate key");
    }

    @Test
    public void testCollisionHandling() {
        try {
            dictionary.insert("Aa", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        try {
            dictionary.insert("BB", "value2");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals("value1", dictionary.find("Aa"), "Collision handling failed for Aa");
        assertEquals("value2", dictionary.find("BB"), "Collision handling failed for BB");
    }

    @Test
    public void testResize() {
        for (int i = 0; i < 100; i++) {
            try {
                dictionary.insert("key" + i, "value" + i);
            } catch (NullValueException e) {
                throw new RuntimeException(e);
            }
        }
        assertEquals(100, dictionary.getSize(), "Resize did not maintain correct size");
    }

    @Test
    public void testIteratorOrder() throws NullValueException {
        dictionary.insert("B", "Banana");
        dictionary.insert("A", "Apple");
        dictionary.insert("C", "Cherry");

        Iterator<String> it = dictionary.iterator();
        assertTrue(it.hasNext(), "Iterator should have next");
        assertEquals("A", it.next(), "First key should be A");
        assertEquals("B", it.next(), "Second key should be B");
        assertEquals("C", it.next(), "Third key should be C");
        assertFalse(it.hasNext(), "Iterator should be exhausted");
    }
}
