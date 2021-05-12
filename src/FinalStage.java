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
    //            return this.currentEvent;
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

    private boolean pushItem() {
        this.numProcessed++;
        this.warehouse.add(this.currentItem);
        this.currentItem = null;
        return true;
    }
}
