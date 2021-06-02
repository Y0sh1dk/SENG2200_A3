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
    private Queue<T> itemsStored;                                       // Items stored
    private HashMap<String, ArrayList<StorageQueueEvent>> hashEvents;   // Hashmap of events per Item
    private String ID;                                                  // ID of stage
    private double lastAverageUpdateTime;                               // Used to calculate avg items in queue
    private double averageItems;                                        // Used to calculate avg items in queue


    /**
     * Constructor when no args are given
     */
    public StorageQueue() {
        this.ID = "Not assigned";
        this.lastAverageUpdateTime = 0;
        this.hashEvents = new HashMap<>();
        this.itemsStored = new LinkedList<>(); // infinite size
    }

    /**
     * Constructor when Id and max size are given
     * @param inID ID of storage queue
     * @param inMaxSize max size of storage queue
     */
    public StorageQueue(String inID, int inMaxSize) {
        this();
        this.ID = inID;
        this.itemsStored = new LimitedLinkedList<>(inMaxSize); // Limited size
    }

    /**
     * getEvents()
     * @return hashmap of events that occurred on this queue
     */
    public HashMap<String, ArrayList<StorageQueueEvent>> getEvents() {
        return this.hashEvents;
    }

    /**
     * updateAverageItems() method
     * Use for calculations of average items in queue
     */
    private void updateAverageItems() {
        this.averageItems += this.size() * (ProductionLine.getConfig().getCurrentTime() - this.lastAverageUpdateTime);
        this.lastAverageUpdateTime = ProductionLine.getConfig().getCurrentTime();
    }

    /**
     * add() method
     * @param inItem Item to add to queue
     * @return boolean, true if successful, else false
     */
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

    /**
     * remove() method
     * @return Item at head of queue if available, else null
     */
    public T remove() {
        // Try to remove a item
        try {
            T removed = this.itemsStored.remove();
            this.generateEvent(removed.getUniqueID(), StorageQueueEvent.Type.REMOVE);
            updateAverageItems();
            return removed;
        }
        // No items to remove
        catch (NoSuchElementException e) {
            this.generateEvent("", StorageQueueEvent.Type.REMOVE_FAILED);
            return null;
        }
    }

    /**
     * generateEvent() method
     * @param itemID Item ID for event
     * @param inType type of event
     */
    private void generateEvent(String itemID, StorageQueueEvent.Type inType) {
        // If new item, create ArrayList for it
        if (!this.hashEvents.containsKey(itemID)) {
            this.hashEvents.put(itemID, new ArrayList<>());
        }
        // add event to ArrayList for that item
        this.hashEvents.get(itemID).add(new StorageQueueEvent(
                this.ID,
                itemID,
                ProductionLine.getConfig().getCurrentTime(),
                inType)
        );
    }

    /**
     * getID() method
     * @return the ID of the queue
     */
    public String getID() {
        return ID;
    }

    /**
     * size() method
     * @return the size of the queue
     */
    public int size() {
        return this.itemsStored.size();
    }

    /**
     * getAverageItems() method
     * @return avg items in the queue
     */
    public double getAverageItems() {
        return this.averageItems;
    }

    /**
     * LimitedLinkedList Class
     * Same as LinkedList, but has a fixed size
     * @param <T> Objects it stores
     */
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

    /**
     * toString() method
     * @return String representation of class
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StorageQueue{");
        sb.append("itemsStored=").append(itemsStored);
        sb.append(", hashEvents=").append(hashEvents);
        sb.append(", ID='").append(ID).append('\'');
        sb.append(", lastAverageUpdateTime=").append(lastAverageUpdateTime);
        sb.append(", averageItems=").append(averageItems);
        sb.append('}');
        return sb.toString();
    }
}
