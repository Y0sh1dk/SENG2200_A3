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
    protected boolean getItem() {
        this.currentItem = prevQueue.remove();
        return this.currentItem != null;
    }


    // TODO(yoshi): can refactor out into abstract class and get implement getItem() and pushItem() in concrete classes
    @Override
    public Double process() {
        // If starved
        switch(this.state) {
            case STARVED:
                if (this.getItem()) {
                    currentItem.setFinishTime(ProductionLine.config.getCurrentTime() + this.calProcessingTime());
                    this.state = State.PROCESSING;
                    return currentItem.getFinishTime();
                }
                break;
            case PROCESSING:
                if (ProductionLine.config.getCurrentTime() == this.currentItem.getFinishTime()) {
                    // we are finished
                    if (this.pushItem()) {
                        this.state = State.STARVED;
                    } else {
                        this.state = State.BLOCKED;
                    }
                }
                break;
            case BLOCKED:
                if (this.pushItem()) {
                    this.state = State.STARVED;
                } else {
                    this.state = State.BLOCKED;
                }
                break;
        }
        return null;
    }


    //@Override
    //public StageEvent process() {
    //    // If starved
    //    if (this.state == State.STARVED) {
    //        if (this.getItem()) {
    //            numProcessed++;
    //            //System.out.println(this.getID() + ": got" + this.currentItem.getUniqueID());
    //            this.state = State.PROCESSING;
    //            this.currentItem.setState(Item.State.PROCESSING);
    //            this.currentEvent = this.genEvent();
    //            return currentEvent;
    //        }
    //    }
    //    else if (this.state == State.PROCESSING || this.state == State.BLOCKED) {
    //        // if finish time is same as last event added finish time
    //        if (ProductionLine.config.getCurrentTime() ==
    //                currentEvent.getFinishTime()) {
    //            this.currentEvent.setFinished(true);
    //            // If can push current item into next queue
    //            if (this.pushItem()) {
    //                this.state = State.STARVED;
    //            } else {
    //                this.state = State.BLOCKED;
    //            }
    //        }
    //    }
    //    return null;
    //}
}
