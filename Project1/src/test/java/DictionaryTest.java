/*
Manav Malik - mmalik37@uic.edu - 654488575
project 1 - implimentation of a dictnery which can have elements of
any type <k,v> , using 2 methods hashtable and binary serch tree and testing the functionality
*/

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

public abstract class DictionaryTest {
    public abstract ProjOneDictionary<String,String> newDictionary();

    @Test
    public void testSize0(){
        ProjOneDictionary<String, String> dict = newDictionary();
        assertEquals(0,dict.getSize());
    }



    @Test
    public void testInsert() throws NullValueException {
        ProjOneDictionary<String, String> dict = newDictionary();
        assertTrue(dict.insert("A", "Apple"), "Inserting new key should return false");
        assertTrue(dict.insert("A", "Avocado"), "Overwriting existing key should return true");
    }

    @Test
    public void testInsertNullValue() {
        ProjOneDictionary<String, String> dict = newDictionary();
        assertThrows(NullValueException.class, () -> dict.insert("A", null));
    }


    @Test
    public void testFind() throws NullValueException {
        ProjOneDictionary<String, String> dict = newDictionary();
        dict.insert("A", "Apple");
        assertEquals( "Apple", dict.find("A") , "Should find the inserted value");
        assertNull( dict.find("B"),"Should return null for non-existent key");
    }

    @Test
    public void testDelete() throws NullValueException {
        ProjOneDictionary<String, String> dict = newDictionary();
        dict.insert("A", "Apple");
        assertTrue(dict.delete("A"), "Deleting existing key should return true");
        assertFalse(dict.delete("B"), "Deleting non-existent key should return false");
        assertNull( dict.find("A"),"Key should no longer be found");
    }

    @Test
    public void testSize() {
        ProjOneDictionary<String, String> dict = newDictionary();
        assertEquals(0, dict.getSize(),"Initial size should be 0");
        try {
            dict.insert("A", "Apple");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals( 1, dict.getSize(),"Size should increase after insertion");
        dict.delete("A");
        assertEquals( 0, dict.getSize(),"Size should decrease after deletion");
    }

    @Test
    public void testIteration() throws NullValueException {
        ProjOneDictionary<String, String> dict = newDictionary();
        dict.insert("B", "Banana");
        dict.insert("A", "Apple");
        dict.insert("C", "Cherry");

        Iterator<String> it = dict.iterator();
        assertTrue( it.hasNext(),"Iterator should have next");
        assertEquals( "A", it.next(),"First key should be A");
        assertEquals( "B", it.next(),"Second key should be B");
        assertEquals( "C", it.next(),"Third key should be C");
        assertFalse( it.hasNext(),"Iterator should be exhausted");
    }

    @Test
    public void testTreeStructureAfterDeletions() throws NullValueException {
        ProjOneDictionary<String, String> dict = newDictionary();
        dict.insert("D", "Dog");
        dict.insert("B", "Banana");
        dict.insert("F", "Fox");
        dict.insert("A", "Apple");
        dict.insert("C", "Cat");
        dict.insert("E", "Elephant");
        dict.insert("G", "Goat");

        // Deleting a node with two children
        assertTrue( dict.delete("B"),"Deleting node with two children should return true");
        assertNull( dict.find("B"),"Key B should no longer exist");
        assertEquals( "Apple", dict.find("A"),"Key A should still exist");
        assertEquals("Cat", dict.find("C"),"Key C should still exist");

        // Deleting root node
        assertTrue( dict.delete("D"),"Deleting root node should return true");
        assertNull( dict.find("D"),"Key D should no longer exist");
        assertEquals( "Elephant", dict.find("E"),"Key E should still exist");
        assertEquals( "Fox", dict.find("F"),"Key F should still exist");
    }







    @Test
    public void testInsertAndFind() {
        ProjOneDictionary<String, String> dictionary = newDictionary();
        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals("value1", dictionary.find("key1"), "Failed to retrieve inserted value");
    }

    @Test
    public void testOverwriteValue() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        try {
            dictionary.insert("key1", "value2");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals("value2", dictionary.find("key1"), "Failed to overwrite value");
    }

    @Test
    public void testDelete2() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertTrue(dictionary.delete("key1"), "Failed to delete key");
        assertNull(dictionary.find("key1"), "Deleted key still found");
    }

    @Test
    public void testDeleteNonExistentKey() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertFalse(dictionary.delete("nonexistent"), "Nonexistent key should return false");
    }

    @Test
    public void testFindNonExistentKey() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertNull(dictionary.find("nonexistent"), "Nonexistent key should return null");
    }

    @Test
    public void testInsertNullKey() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertThrows(NullValueException.class, () -> {
            dictionary.insert(null, "value");
        }, "Expected a NullValueException when inserting null key");

    }

    @Test
    public void testInsertNullValue2() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertThrows(NullValueException.class, () -> {
            dictionary.insert("key1", null);}, "Expected a NullValueException when inserting null key");
    }

    @Test
    public void testSize2() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertEquals(0, dictionary.getSize(), "Initial size should be zero");
        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        try {
            dictionary.insert("key2", "value2");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, dictionary.getSize(), "Size mismatch after insertions");
    }

    @Test
    public void testIsEmpty() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        assertEquals(0,dictionary.getSize(), "New dictionary should be empty");
        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1,dictionary.getSize(), "Dictionary should not be empty after insertion");
    }

    @Test
    public void testCollisionHandling() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

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
        assertEquals("value1", dictionary.find("Aa"), "Collision handling failed");
        assertEquals("value2", dictionary.find("BB"), "Collision handling failed");
    }

    @Test
    public void testResize() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

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
    public void testIterator() {
        ProjOneDictionary<String, String> dictionary = newDictionary();

        try {
            dictionary.insert("key1", "value1");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        try {
            dictionary.insert("key2", "value2");
        } catch (NullValueException e) {
            throw new RuntimeException(e);
        }
        Iterator<String> iterator = dictionary.iterator();
        assertNotNull(iterator, "Iterator should not be null");
        int count = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            assertNotNull(dictionary.find(key), "Iterator returned nonexistent key");
            count++;
        }
        assertEquals(2, count, "Iterator did not iterate over all elements");
    }
}


