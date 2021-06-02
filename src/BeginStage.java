/**
 * FileName: BeginStage.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a starting stage in the production line
 */

public class BeginStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> nextQueue;              // Queue to push items too

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
    @Override
    protected boolean pushItem() {
        if (this.nextQueue.add(this.currentItem)) {
            this.numProcessed++;
            this.currentItem = null;
            if (this.state == State.BLOCKED) {
                this.totalBlockedTime += (ProductionLine.getConfig().getCurrentTime() - this.lastBlockedTime);
            }
            this.lastBlockedTime = 0;
            return true;
        }
        if (this.state != State.BLOCKED) {
            this.lastBlockedTime = ProductionLine.getConfig().getCurrentTime();
        }
        return false;
    }

    /**
     * getItem() method
     * Only way to generate `T` instances within `BeginStage` is to do a unchecked cast to `T`,
     * java dont like unchecked casts and wont compile so the warning is suppressed :)
     * Me > Compiler
     * @return boolean true if got a item, false if not (always true because beginning stage)
     */
    @Override
    @SuppressWarnings("unchecked")
    protected boolean getItem() {
        this.currentItem = (T) new Item((UniqueItemID.getID()));
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BeginStage{");
        sb.append("nextQueue=").append(nextQueue);
        sb.append('}');
        return sb.toString();
    }
}
