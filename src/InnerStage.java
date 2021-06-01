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
            return true;
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
        return this.currentItem != null;
    }


}
