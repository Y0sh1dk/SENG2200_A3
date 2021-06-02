/**
 * FileName: InnerStage.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a inner stage in the production line
 */
public class InnerStage<T extends Item> extends AbstractStage<T> {
    protected StorageQueue<T> prevQueue;                // Previous queue to get items from
    protected StorageQueue<T> nextQueue;                // Next queue to push items too

    public InnerStage(String inID) {
        super(inID, State.STARVED);
        this.prevQueue = new StorageQueue<>();
        this.nextQueue = new StorageQueue<>();
    }

    /**
     * setPrevQueue() method
     * @param inQueue queue to set as previous queue
     */
    public void setPrevQueue(StorageQueue<T> inQueue) {
        this.prevQueue = inQueue;
    }

    /**
     * setNextQueue() method
     * @param inQueue queue to set as next queue
     */
    public void setNextQueue(StorageQueue<T> inQueue) {
        this.nextQueue = inQueue;
    }

    /**
     * getPrevQueue() method
     * @return the previous queue
     */
    public StorageQueue<T> getPrevQueue() {
        return this.prevQueue;
    }

    /**
     * getNextQueue() method
     * @return the next queue
     */
    public StorageQueue<T> getNextQueue() {
        return this.nextQueue;
    }


    /**
     * Push item into next queue
     */
    @Override
    protected boolean pushItem() {
        if (this.nextQueue.add(this.currentItem)) {
            this.numProcessed++;
            this.currentItem = null;
            if (this.state == State.BLOCKED) {
                this.totalBlockedTime += (ProductionLine.getConfig().getCurrentTime() - this.lastBlockedTime);
            }
            this.lastBlockedTime = 0;
            return true;
        }
        if (this.state != State.BLOCKED) {
            this.lastBlockedTime = ProductionLine.getConfig().getCurrentTime();
        }
        return false;
    }

    /**
     * get item from prev queue if item available and sets state
     * @return true if successful, else false
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
     * toString() method
     * @return String representation of class
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InnerStage{");
        sb.append("prevQueue=").append(prevQueue);
        sb.append(", nextQueue=").append(nextQueue);
        sb.append('}');
        return sb.toString();
    }
}
