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
    public void process() {
        // If starved
        if (this.state == State.STARVED) {
            numProcessed++;
            this.currentItem = this.generateItem();
            this.state = State.PROCESSING;
            this.currentItem.setState(Item.State.PROCESSING);
            //TODO: return this from method
            this.currentEvent = this.genEvent();
        }
        else if (this.state == State.PROCESSING) {
            // if finish time is same as last event added finish time
            if (ProductionLine.config.getCurrentTime() ==
                    this.currentEvent.getFinishTime()) {
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
