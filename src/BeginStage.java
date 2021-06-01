public class BeginStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> nextQueue;

    public BeginStage(String inID) {
        super(inID, State.STARVED);
        this.nextQueue = new StorageQueue<>();
    }

    public void setNextQueue(StorageQueue<T> inQueue) {
        this.nextQueue = inQueue;
    }

    public StorageQueue<T> getNextQueue() {
        return this.nextQueue;
    }


    /**
     * Push item into next queue
     */
    //TODO(yoshi): refactor
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
     * TODO(yoshi): document this!!!!!!!!!!!!!!!
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected boolean getItem() {
        this.currentItem = (T) new Item((UniqueItemID.getID()));
        return true;
    }
}
