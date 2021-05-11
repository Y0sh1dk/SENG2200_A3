import java.util.ArrayList;

public class FinalStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> prevQueue;
    private ArrayList<T> warehouse;

    public FinalStage(String inID) {
        super(inID);
        this.prevQueue = new StorageQueue<>();
        this.warehouse = new ArrayList<>();
    }

    public void setPrevQueue(StorageQueue<T> inQueue) {
        this.prevQueue = inQueue;
    }

    public StorageQueue<T> getPrevQueue() {
        return this.prevQueue;
    }


}
