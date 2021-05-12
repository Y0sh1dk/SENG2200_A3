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

    @Override
    public Double process() {
        // If starved
        switch(this.state) {
            case STARVED:
                this.numProcessed++;
                this.currentItem = generateItem();
                currentItem.setFinishTime(ProductionLine.config.getCurrentTime() + this.calProcessingTime());
                this.state = State.PROCESSING;
                return currentItem.getFinishTime();
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
                    this.numProcessed++;
                } else {
                    this.state = State.BLOCKED;
                }
                break;
        }
        return null;
    }




    //public StageEvent process() {
    //    // If starved
    //    if (this.state == State.STARVED) {
    //        numProcessed++;
    //        this.currentItem = this.generateItem();
    //        this.state = State.PROCESSING;
    //        this.currentItem.setState(Item.State.PROCESSING);
    //        this.currentEvent = this.genEvent();
    //        return currentEvent;
    //    }
    //    else if (this.state == State.PROCESSING) {
    //        // if finish time is same as last event added finish time
    //        if (ProductionLine.config.getCurrentTime() ==
    //                this.currentEvent.getFinishTime()) {
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
     * TODO(yoshi): document this!!!!!!!!!!!!!!!
     * @return
     */
    @SuppressWarnings("unchecked")
    private T generateItem() {
        T item = (T) new Item((UniqueItemID.getID()));
        item.setState(Item.State.PROCESSING);
        return item;
    }
}
