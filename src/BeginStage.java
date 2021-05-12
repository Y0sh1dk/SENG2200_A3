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
    @Override
    @SuppressWarnings("unchecked")
    protected boolean getItem() {
        this.currentItem = (T) new Item((UniqueItemID.getID()));
        return true;
    }
}
