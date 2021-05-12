import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class StorageQueue<T extends Item> {
    private Queue<T> itemsStored;
    private ArrayList<StorageQueueEvent> events;
    private String ID;


    public StorageQueue() {
        this.ID = "Not assigned";
        this.events = new ArrayList<>();
        this.itemsStored = new LinkedList<>(); // infinite size
    }


    public StorageQueue(String inID, int inMaxSize) {
        this();
        this.ID = inID;
        this.itemsStored = new LimitedLinkedList<>(inMaxSize); // Limited size
    }


    public boolean add(T inItem) {
        boolean isAdded =  this.itemsStored.add(inItem);
        if (isAdded) {
            this.events.add(new StorageQueueEvent(
                    this.ID,
                    inItem.getUniqueID(),
                    ProductionLine.config.getCurrentTime(),
                    StorageQueueEvent.Type.ADD));
        } else {
            this.events.add(new StorageQueueEvent(
                    this.ID,
                    inItem.getUniqueID(),
                    ProductionLine.config.getCurrentTime(),
                    StorageQueueEvent.Type.ADD_FAILED));
        }
        return isAdded;
    }

    // Returns null if no such item
    public T remove() {
        try {
            T removed = this.itemsStored.remove();
            this.events.add(new StorageQueueEvent(
                    this.ID,
                    removed.getUniqueID(),
                    ProductionLine.config.getCurrentTime(),
                    StorageQueueEvent.Type.REMOVE));
            return removed;
        } catch (NoSuchElementException e) {
            this.events.add(new StorageQueueEvent(
                    this.ID,
                    "",
                    ProductionLine.config.getCurrentTime(),
                    StorageQueueEvent.Type.REMOVE_FAILED));
            return null;
        }
    }

    public int size() {
        return this.itemsStored.size();
    }


    // Lmao, lachlan will hate this
    // TODO(yoshi): test yo
    private class LimitedLinkedList<T> extends LinkedList<T> {
        private final int limit;

        public LimitedLinkedList(int inLimit) {
            this.limit = inLimit;
        }

        @Override
        public boolean add(T o) {
            if (this.size() == limit) {
                return false;
            }
            super.add(o);
            return true;
        }

    }
}
