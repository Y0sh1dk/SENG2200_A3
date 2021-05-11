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
            this.currentItem = this.generateItem();
        }
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
