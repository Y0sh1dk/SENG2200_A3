/**
 * FileName: AbstractStage.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Abstract stage class to represent a stage in the production line
 */

public abstract class AbstractStage<T extends Item> {
    protected double totalStarvedTime;          // Time spent in starved stage
    protected double totalBlockedTime;          // Time spent in blocked state
    protected double lastStarvedTime;           // Last time starved (resets to 0 when becomes unstarved)
    protected double lastBlockedTime;           // Last time blocked (resets to 0 when becomes unblocked)
    private String ID;                          // ID of stage
    protected int numProcessed;                 // Number of items processed
    protected T currentItem;                    // Current item in stage
    protected State state;                      // state of the stage
    protected double multiplier;                // Multiplier (as defined in spec)

    public double getTotalStarvedTime() {
        return this.totalStarvedTime;
    }

    public double getTotalBlockedTime() {
        return this.totalBlockedTime;
    }

    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        this.totalBlockedTime = 0;
        this.totalStarvedTime = 0;
        this.state = State.STARVED;
        this.multiplier = 1;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState) {
        this();
        this.ID = inID;
        this.state = inStartingState;
    }

    // If got a object, returns its finish time
    // If pushed a object, returns a -1
    // if got blocked, returns 0
    protected Double process() {
        switch(this.state) {
            case STARVED:
                if (this.getItem()) {
                    currentItem.setFinishTime(ProductionLine.getConfig().getCurrentTime() + this.calProcessingTime());
                    this.state = State.PROCESSING;
                    currentItem.getItemPath().add(this.ID);
                    return currentItem.getFinishTime();
                }
                break;
            case PROCESSING:
                if (ProductionLine.getConfig().getCurrentTime() == this.currentItem.getFinishTime()) {
                    // we are finished
                    if (this.pushItem()) {
                        this.state = State.STARVED;
                        return (double) -1;
                    } else {
                        this.state = State.BLOCKED;
                    }
                }
                break;
            case BLOCKED:
                if (this.pushItem()) {
                    this.state = State.STARVED;
                    return (double) -1;
                } else {
                    this.state = State.BLOCKED;
                }
                break;
        }
        return (double) 0;
    }

    protected abstract boolean pushItem();

    protected abstract boolean getItem();


    protected double calProcessingTime() {
        return ((ProductionLine.getConfig().getM()*multiplier) +
                (ProductionLine.getConfig().getN()*multiplier) * (ProductionLine.getRandomInst().nextDouble() - 0.5));
    }

    public State getState() {
        return this.state;
    }

    public String getID() {
        return this.ID;
    }

    public void setMultiplier(double inMulti) {
        this.multiplier = inMulti;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractStage{");
        sb.append("ID='").append(ID).append('\'');
        sb.append(", numProcessed=").append(numProcessed);
        sb.append(", currentItem=").append(currentItem);
        sb.append(", state=").append(state);
        sb.append(", multiplier=").append(multiplier);
        sb.append('}');
        return sb.toString();
    }
}
