import java.util.*;

/**
 * FileName: StorageQueue.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a storage queue between stages in the production line
 */
public class StorageQueue<T extends Item> {
    private Queue<T> itemsStored;
    private ArrayList<StorageQueueEvent> events;
    private HashMap<String, ArrayList<StorageQueueEvent>> hashEvents;
    private String ID;
    private double lastAverageUpdateTime;
    private double averageItems;


    public StorageQueue() {
        this.ID = "Not assigned";
        this.lastAverageUpdateTime = 0;
        this.events = new ArrayList<>();
        this.hashEvents = new HashMap<>();
        this.itemsStored = new LinkedList<>(); // infinite size
    }


    public StorageQueue(String inID, int inMaxSize) {
        this();
        this.ID = inID;
        this.itemsStored = new LimitedLinkedList<>(inMaxSize); // Limited size
    }

    public HashMap<String, ArrayList<StorageQueueEvent>> getEvents() {
        return hashEvents;
    }

    public void updateAverageItems() {
        this.averageItems += this.size() * (ProductionLine.config.getCurrentTime() - this.lastAverageUpdateTime);
        this.lastAverageUpdateTime = ProductionLine.config.getCurrentTime();
    }



    public boolean add(T inItem) {
        boolean isAdded =  this.itemsStored.add(inItem);
        if (isAdded) {
            this.generateEvent(inItem.getUniqueID(), StorageQueueEvent.Type.ADD);
            updateAverageItems();
        } else {
            this.generateEvent(inItem.getUniqueID(), StorageQueueEvent.Type.ADD_FAILED);
        }
        return isAdded;
    }

    // Returns null if no such item
    public T remove() {
        try {
            T removed = this.itemsStored.remove();
            this.generateEvent(removed.getUniqueID(), StorageQueueEvent.Type.REMOVE);
            updateAverageItems();
            return removed;
        } catch (NoSuchElementException e) {
            this.generateEvent("", StorageQueueEvent.Type.REMOVE_FAILED);
            return null;
        }
    }


    private void generateEvent(String itemID, StorageQueueEvent.Type inType) {
        // If new item, create arrayList for it
        if (!this.hashEvents.containsKey(itemID)) {
            this.hashEvents.put(itemID, new ArrayList<>());
        }
        this.hashEvents.get(itemID).add(new StorageQueueEvent(
                this.ID,
                itemID,
                ProductionLine.config.getCurrentTime(),
                inType)
        );
    }

    public String getID() {
        return ID;
    }

    public int size() {
        return this.itemsStored.size();
    }

    public double getAverageItems() {
        return averageItems;
    }

    private static class LimitedLinkedList<T> extends LinkedList<T> {
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StorageQueue{");
        sb.append("itemsStored=").append(itemsStored);
        sb.append(", events=").append(events);
        sb.append(", ID='").append(ID).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
