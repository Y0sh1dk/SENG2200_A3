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
    //TODO(yoshi): refactor
    private boolean pushItem() {
        boolean temp = this.nextQueue.add(this.currentItem);
        if (temp) {
            this.currentItem = null;
            return true;
        }
        return false;
    }

    /**
     * get item from prev queue if item available and sets state
     * @return true if successful, else false
     */
    protected boolean getItem() {
        if (this.prevQueue.size() != 0) {
            this.currentItem = prevQueue.remove();
            return true;
        }
        return false;
    }

    @Override
    public void process() {
        // If starved
        if (this.state == State.STARVED) {
            if (this.getItem()) {
                numProcessed++;
                //System.out.println(this.getID() + ": got" + this.currentItem.getUniqueID());
                this.state = State.PROCESSING;
                this.currentItem.setState(Item.State.PROCESSING);
                this.currentEvent = this.genEvent();
            }
        }
        else if (this.state == State.PROCESSING || this.state == State.BLOCKED) {
            // if finish time is same as last event added finish time
            if (ProductionLine.config.getCurrentTime() ==
                    currentEvent.getFinishTime()) {
                this.currentEvent.setFinished(true);
                // If can push current item into next queue
                if (this.pushItem()) {
                    this.state = State.STARVED;
                } else {
                    this.state = State.BLOCKED;
                }
            }
        }

    }
}
