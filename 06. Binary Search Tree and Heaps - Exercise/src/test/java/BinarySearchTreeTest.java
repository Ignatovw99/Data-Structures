import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BinarySearchTreeTest {

    private BinarySearchTree<Integer> binarySearchTree;

    @Before
    public void setup() {
        binarySearchTree = new BinarySearchTree<>();
        List<Integer> elements = new ArrayList<>(List.of(15, 25, 6, 9, 5, 8, 17, 16));
        for (Integer element : elements) {
            binarySearchTree.insert(element);
        }
    }

    @Test
    public void insertShouldOrderElementsRight() {
        List<Integer> orderedElements = new ArrayList<>(List.of(5, 6, 8, 9, 15, 16, 17, 25));
        List<Integer> bstInOrderResult = new ArrayList<>();

        binarySearchTree.eachInOrder(bstInOrderResult::add);

        assertEquals(binarySearchTree.count(), bstInOrderResult.size());
        for (int i = 0; i < orderedElements.size(); i++) {
            assertEquals(orderedElements.get(i), bstInOrderResult.get(i));
        }
    }

    @Test
    public void insertShouldIncreaseCount() {
        assertEquals(8, binarySearchTree.count());
        binarySearchTree.insert(67);
        assertEquals(9, binarySearchTree.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMinShouldThrowExceptionIfTreeIsEmpty() {
        BinarySearchTree<Integer> emptyBinarySearchTree = new BinarySearchTree<>();
        emptyBinarySearchTree.deleteMin();
    }

    @Test
    public void deleteMinShouldDeleteRootNodeIfThereIsOnlyOneElement() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.insert(15);
        assertTrue(binarySearchTree.contains(15));
        assertNotNull(binarySearchTree.getRoot());
        binarySearchTree.deleteMin();
        assertFalse(binarySearchTree.contains(15));
        assertNull(binarySearchTree.getRoot());
    }

    @Test
    public void deleteMinShouldDeleteMinElement() {
        assertTrue(binarySearchTree.contains(5));
        binarySearchTree.deleteMin();
        assertFalse(binarySearchTree.contains(5));
    }

    @Test
    public void deleteMinShouldDeleteElementsCorrectlyForMultipleElements() {
        assertTrue(binarySearchTree.contains(5));
        binarySearchTree.deleteMin();
        assertFalse(binarySearchTree.contains(5));
        assertTrue(binarySearchTree.contains(6));
        binarySearchTree.deleteMin();
        assertFalse(binarySearchTree.contains(6));
        assertTrue(binarySearchTree.contains(8));
        binarySearchTree.deleteMin();
        assertFalse(binarySearchTree.contains(8));
    }

    @Test
    public void deleteMinShouldDecreaseCount() {
        assertEquals(8, binarySearchTree.count());
        binarySearchTree.deleteMin();
        assertEquals(7, binarySearchTree.count());
    }

    @Test
    public void deleteMinShouldNotDecreaseCountIfTreeIsEmpty() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        try{
            binarySearchTree.deleteMin();
        } catch (IllegalArgumentException ignored) {
            ;
        }
        assertEquals(0, binarySearchTree.count());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMaxShouldThrowExceptionIfTreeIsEmpty() {
        BinarySearchTree<Integer> emptyBinarySearchTree = new BinarySearchTree<>();
        emptyBinarySearchTree.deleteMax();
    }

    @Test
    public void deleteMaxShouldDeleteRootNodeIfThereIsOnlyOneElement() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.insert(15);
        assertTrue(binarySearchTree.contains(15));
        assertNotNull(binarySearchTree.getRoot());
        binarySearchTree.deleteMax();
        assertFalse(binarySearchTree.contains(15));
        assertNull(binarySearchTree.getRoot());
    }

    @Test
    public void deleteMaxShouldDeleteMaxElement() {
        assertTrue(binarySearchTree.contains(25));
        binarySearchTree.deleteMax();
        assertFalse(binarySearchTree.contains(25));
    }

    @Test
    public void deleteMaxShouldDeleteElementsCorrectlyForMultipleElements() {
        assertTrue(binarySearchTree.contains(25));
        binarySearchTree.deleteMax();
        assertFalse(binarySearchTree.contains(25));
        assertTrue(binarySearchTree.contains(17));
        binarySearchTree.deleteMax();
        assertFalse(binarySearchTree.contains(17));
        assertTrue(binarySearchTree.contains(16));
        binarySearchTree.deleteMax();
        assertFalse(binarySearchTree.contains(16));
    }

    @Test
    public void deleteMaxShouldDecreaseCount() {
        assertEquals(8, binarySearchTree.count());
        binarySearchTree.deleteMax();
        assertEquals(7, binarySearchTree.count());
    }

    @Test
    public void deleteMaxShouldNotDecreaseCountIfTreeIsEmpty() {
        BinarySearchTree<Integer> binarySearchTree = new BinarySearchTree<>();
        try {
            binarySearchTree.deleteMax();
        } catch (IllegalArgumentException ignored) {
            ;
        }
        assertEquals(0, binarySearchTree.count());
    }

    @Test
    public void rankShouldWorkCorrectlyWithElementsInsideTheTree() {
        assertEquals(4, binarySearchTree.rank(15));
        assertEquals(5, binarySearchTree.rank(16));
        assertEquals(2, binarySearchTree.rank(8));
        assertEquals(2, binarySearchTree.rank(7));
    }

    @Test
    public void rankShouldReturnZeroIfTheTreeIsEmpty() {
        assertEquals(0, new BinarySearchTree<Integer>().rank(1));
    }

    @Test
    public void rankShouldReturnZeroIfInsideTheTreeThereIsNotSmallerValue() {
        assertEquals(0, binarySearchTree.rank(-17));
    }

    @Test
    public void ceilTest() {
        assertEquals(Integer.valueOf(17), binarySearchTree.ceil(16));
        assertEquals(Integer.valueOf(9), binarySearchTree.ceil(8));
        assertNull(binarySearchTree.ceil(30));
    }

    @Test
    public void floorTest() {
        assertEquals(Integer.valueOf(6), binarySearchTree.floor(7));
        assertEquals(Integer.valueOf(17), binarySearchTree.floor(20));
        assertEquals(Integer.valueOf(25), binarySearchTree.floor(30));
        assertNull(binarySearchTree.floor(-4));
    }
}
