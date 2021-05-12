import java.util.ArrayList;

public class FinalStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> prevQueue;
    private ArrayList<T> warehouse;

    public FinalStage(String inID) {
        super(inID, State.STARVED);
        this.prevQueue = new StorageQueue<>();
        this.warehouse = new ArrayList<>();
    }

    public void setPrevQueue(StorageQueue<T> inQueue) {
        this.prevQueue = inQueue;
    }

    public StorageQueue<T> getPrevQueue() {
        return this.prevQueue;
    }

    public void setWarehouse(ArrayList<T> warehouse) {
        this.warehouse = warehouse;
    }

    public ArrayList<T> getWarehouse() {
        return this.warehouse;
    }

    @Override
    protected boolean getItem() {
        if (this.prevQueue.size() != 0) {
            this.currentItem = prevQueue.remove();
            return true;
        }
        return false;
    }

    @Override
    protected boolean pushItem() {
        this.numProcessed++;
        this.warehouse.add(this.currentItem);
        this.currentItem = null;
        return true;
    }
}
