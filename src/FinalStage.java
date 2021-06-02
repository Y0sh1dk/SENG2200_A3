import java.util.ArrayList;

/**
 * FileName: FinalStage.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a finishing stage in the production line
 */
public class FinalStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> prevQueue;              // Previous queue to get items from
    private ArrayList<T> warehouse;                 // Warehouse to push items too

    /**
     * Constructor when ID is given
     * @param inID
     */
    public FinalStage(String inID) {
        super(inID, State.STARVED);
        this.prevQueue = new StorageQueue<>();
        this.warehouse = new ArrayList<>();
    }

    /**
     * setPrevQueue() method
     * @param inQueue queue to set as previous queue
     */
    public void setPrevQueue(StorageQueue<T> inQueue) {
        this.prevQueue = inQueue;
    }

    /**
     * getPrevQueue() method
     * @return the previous queue
     */
    public StorageQueue<T> getPrevQueue() {
        return this.prevQueue;
    }

    /**
     * setWarehouse() method
     * @param warehouse ArrayList to set as warehouse of stage
     */
    public void setWarehouse(ArrayList<T> warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * getWarehouse() method
     * @return the ArrayList of finished items
     */
    public ArrayList<T> getWarehouse() {
        return this.warehouse;
    }

    /**
     * getItem()
     * Attempts to get an item from the previous queue, updates starved time
     * @return boolean, true if successful, else false
     */
    @Override
    protected boolean getItem() {
        this.currentItem = prevQueue.remove();
        // If we got a item
        if (this.currentItem != null) {
            if (this.lastStarvedTime != 0) {
                this.totalStarvedTime += (ProductionLine.getConfig().getCurrentTime() - this.lastStarvedTime);
            }
            this.lastStarvedTime = 0;
            return true;
        }
        // didnt get a item
        if (this.lastStarvedTime == 0) {
            this.lastStarvedTime = ProductionLine.getConfig().getCurrentTime();
        }
        return false;
    }

    /**
     * pushItem() method
     * Pushes an item into the warehouse
     * @return true
     */
    @Override
    protected boolean pushItem() {
        this.numProcessed++;
        this.warehouse.add(this.currentItem);
        this.currentItem = null;
        return true;
    }

    /**
     * toString() method
     * @return String representation of class
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FinalStage{");
        sb.append("prevQueue=").append(prevQueue);
        sb.append(", warehouse=").append(warehouse);
        sb.append('}');
        return sb.toString();
    }
}
