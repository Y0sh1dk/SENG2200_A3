import java.util.ArrayList;

/**
 * FileName: FinalStage.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a finishing stage in the production line
 */
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
        this.currentItem = prevQueue.remove();
        // If we got a item
        if (this.currentItem != null) {
            if (this.lastStarvedTime != 0) {
                this.totalStarvedTime += (ProductionLine.config.getCurrentTime() - this.lastStarvedTime);
            }
            this.lastStarvedTime = 0;
            return true;
        }
        // didnt get a item
        if (this.lastStarvedTime == 0) {
            this.lastStarvedTime = ProductionLine.config.getCurrentTime();
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
