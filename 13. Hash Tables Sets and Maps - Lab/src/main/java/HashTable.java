import java.lang.reflect.Array;
import java.util.*;

public class HashTable<K, V> implements Iterable<KeyValue<K, V>> {

    private final static int INITIAL_CAPACITY = 16;

    private final static double LOAD_FACTOR = 0.80d;

    private LinkedList<KeyValue<K, V>>[] slots;

    private int count;

    private int capacity;

    public HashTable() {
        this(INITIAL_CAPACITY);
    }

    public HashTable(int capacity) {
        this.slots = new LinkedList[capacity];
        count = 0;
        this.capacity = capacity;
    }

    public void add(K key, V value) {
        growIfNeeded();
        int slotIndex = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> slotList = slots[slotIndex];
        if (slotList == null) {
            slotList = new LinkedList<>();
        }

        for (KeyValue<K, V> element : slotList) {
            if (element.getKey().equals(key)) {
                throw new IllegalArgumentException("Key already exists: " + key);
            }
        }

        KeyValue<K, V> keyValuePairToInsert = new KeyValue<>(key, value);
        slotList.addLast(keyValuePairToInsert);
        slots[slotIndex] = slotList;
        count++;
    }

    private int findSlotNumber(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    private void growIfNeeded() {
        if ((double) (count + 1) / capacity > LOAD_FACTOR) {
            grow();
        }
    }

    private void grow() {
        HashTable<K, V> newHashTable = new HashTable<>(capacity * 2);

        //Here insead of using iterator we can use the slots hash table to iterate through
        for (KeyValue<K, V> oldKeyValuePair : this) {
            newHashTable.add(oldKeyValuePair.getKey(), oldKeyValuePair.getValue());
        }

        slots = newHashTable.slots;
        capacity = newHashTable.capacity;
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return capacity;
    }

    public boolean addOrReplace(K key, V value) {
        growIfNeeded();
        int slotIndex = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> slotList = slots[slotIndex];
        if (slotList == null) {
            slotList = new LinkedList<>();
        }

        boolean replaced = false;

        for (KeyValue<K, V> element : slotList) {
            if (element.getKey().equals(key)) {
                element.setValue(value);
                replaced = true;
            }
        }

        if (!replaced) {
            KeyValue<K, V> keyValuePairToInsert = new KeyValue<>(key, value);
            slotList.addLast(keyValuePairToInsert);
            count++;
        }

        slots[slotIndex] = slotList;

        return replaced;
    }

    public V get(K key) {
        KeyValue<K, V> foundKeyValuePair = find(key);
        if (foundKeyValuePair == null) {
            throw new IllegalArgumentException();
        }
        return foundKeyValuePair.getValue();
    }

    public KeyValue<K, V> find(K key) {
        int slotIndex = findSlotNumber(key);
        LinkedList<KeyValue<K, V>> slotElements = slots[slotIndex];
        if (slotElements != null) {
            for (KeyValue<K, V> element : slotElements) {
                if (element.getKey().equals(key)) {
                    return element;
                }
            }
        }
        return null;
    }

    public boolean containsKey(K key) {
        return find(key) != null;
    }

    public boolean remove(K key) {
        int slotIndex = findSlotNumber(key);

        LinkedList<KeyValue<K, V>> slotElements = slots[slotIndex];

        if (slotElements == null) {
            return false;
        }

        KeyValue<K, V> toRemove = null;
        for (KeyValue<K, V> element : slotElements) {
            if (element.getKey().equals(key)) {
                toRemove = element;
                break;
            }
        }

        boolean isRemoved = toRemove != null && slotElements.remove(toRemove);

        if (isRemoved) {
            count--;
        }

        return isRemoved;
    }

    public void clear() {
        capacity = INITIAL_CAPACITY;
        slots = new LinkedList[capacity];
        count = 0;
    }

    public Iterable<K> keys() {
        List<K> keys = new ArrayList<>();
        for (KeyValue<K, V> keyValuePair : this) {
            keys.add(keyValuePair.getKey());
        }
        return keys;
    }

    public Iterable<V> values() {
        List<V> values = new ArrayList<>();
        for (KeyValue<K, V> keyValuePair : this) {
            values.add(keyValuePair.getValue());
        }
        return values;
    }

    @Override
    public Iterator<KeyValue<K, V>> iterator() {
        return new Iterator<KeyValue<K, V>>() {

            int passedThroughCount = 0;
            int currentIndex = 0;
            Deque<KeyValue<K, V>> slotElements = new ArrayDeque<>();

            @Override
            public boolean hasNext() {
                return passedThroughCount < count;
            }

            @Override
            public KeyValue<K, V> next() {
                if (!slotElements.isEmpty()) {
                    passedThroughCount++;
                    return slotElements.poll();
                }

                while (slots[currentIndex] == null) {
                    currentIndex++;
                }

                for (KeyValue<K, V> element : slots[currentIndex]) {
                    slotElements.offer(element);
                }

                currentIndex++;
                passedThroughCount++;
                return slotElements.poll();
            }
        };
    }
}
