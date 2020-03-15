package implementations;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ReversedListTest {

    private interfaces.ReversedList<Integer> reversedList;

    @Before
    public void setup() {
        reversedList = new ReversedList<>();
    }

    @Test
    public void add_multipleElements_shouldIncreaseSize() {
        reversedList.add(12);
        reversedList.add(13);
        assertEquals(2, reversedList.size());
    }

    @Test
    public void add_moreElementsThanAvailableCapacity_shouldDoubleElementsCapacity() {
        reversedList.add(12);
        reversedList.add(13);
        reversedList.add(14);
        assertEquals(4, reversedList.capacity());
    }

    @Test
    public void get_shouldReturnCorrectElementInsideTheRange() {
        reversedList.add(12);
        reversedList.add(13);
        reversedList.add(14);
        assertEquals(14, reversedList.get(0).intValue());
        assertEquals(12, reversedList.get(reversedList.size() - 1).intValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void get_shouldThrowExceptionWhenAccessIndexOutOfBounds() {
        reversedList.add(12);
        reversedList.add(13);
        reversedList.add(14);
        reversedList.get(4);
        reversedList.get(-1);
    }

    @Test
    public void removeAt_indexElementsBounds_shouldDecrementSize() {
        reversedList.add(12);
        reversedList.add(13);
        reversedList.add(14);
        reversedList.removeAt(1);
        assertEquals(2, reversedList.size());
    }

    @Test
    public void testIterator() {
        List<Integer> elements = Arrays.asList(12, 13, 14, 15, 16);
        reversedList.add(elements.get(0));
        reversedList.add(elements.get(1));
        reversedList.add(elements.get(2));
        reversedList.add(elements.get(3));
        reversedList.add(elements.get(4));

        int elementsIndex = 4;
        for (Integer element : reversedList) {
            assertEquals(elements.get(elementsIndex--), element);
        }
    }
}