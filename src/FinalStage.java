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

    private boolean pushItem() {
        this.warehouse.add(this.currentItem);
        this.currentItem = null;
        return true;
    }
}
