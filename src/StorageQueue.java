import java.util.LinkedList;
import java.util.Queue;

public class StorageQueue<T extends Item> {
    private Queue<T> itemsStored;
    private String ID;


    public StorageQueue() {
        this.ID = "Not assigned";
    }

    public StorageQueue(String inID) {
        this();
        this.ID = inID;
    }

    public StorageQueue(int inMaxSize) {
        this();
        this.itemsStored = new LimitedLinkedList<T>(inMaxSize);

    }


    public boolean add(T inItem) {
        return this.itemsStored.add(inItem);
    }

    public T remove() {
        return this.itemsStored.remove();
    }


    // Lmao, lachlan will hate this
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
