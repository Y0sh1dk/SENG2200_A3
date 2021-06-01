public class InnerStage<T extends Item> extends AbstractStage<T> {
    protected StorageQueue<T> prevQueue;
    protected StorageQueue<T> nextQueue;

    public InnerStage(String inID) {
        super(inID, State.STARVED);
        this.prevQueue = new StorageQueue<>();
        this.nextQueue = new StorageQueue<>();
    }

    public void setPrevQueue(StorageQueue<T> inQueue) {
        this.prevQueue = inQueue;
    }

    public void setNextQueue(StorageQueue<T> inQueue) {
        this.nextQueue = inQueue;
    }

    public StorageQueue<T> getPrevQueue() {
        return this.prevQueue;
    }

    public StorageQueue<T> getNextQueue() {
        return this.nextQueue;
    }


    /**
     * Push item into next queue
     */
    //TODO(yoshi): use a decorator for incrementing??
    @Override
    protected boolean pushItem() {
        if (this.nextQueue.add(this.currentItem)) {
            this.numProcessed++;
            this.currentItem = null;
            if (this.state == State.BLOCKED) {
                this.totalBlockedTime += (ProductionLine.config.getCurrentTime() - this.lastBlockedTime);
            }
            this.lastBlockedTime = 0;
            return true;
        }
        if (this.state != State.BLOCKED) {
            this.lastBlockedTime = ProductionLine.config.getCurrentTime();
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
                this.totalStarvedTime += (ProductionLine.config.getCurrentTime() - this.lastStarvedTime);
            }
            this.lastStarvedTime = 0;
            return true;
        }
        // didnt get a item
        if (this.lastStarvedTime == 0) {
            this.lastStarvedTime = ProductionLine.config.getCurrentTime();
        }
        return false;
    }


}
