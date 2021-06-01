
public abstract class AbstractStage<T extends Item> {
    protected double totalStarvedTime;
    protected double totalBlockedTime;
    protected double lastStarvedTime;
    protected double lastBlockedTime;
    private String ID;
    protected int numProcessed;
    protected boolean isEventAvailable;
    protected T currentItem;
    protected State state;
    protected double multiplier;

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
        this.isEventAvailable = false;
        this.multiplier = 1;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState) {
        this();
        this.ID = inID;
        this.state = inStartingState;
    }

    // If got a object, returns its finish time
    // If pushed a object, returns a -1 TODO(yoshi) make this a static class attribute
    // if got blocked, returns 0
    protected Double process() {
        switch(this.state) {
            case STARVED:
                if (this.getItem()) {
                    currentItem.setFinishTime(ProductionLine.config.getCurrentTime() + this.calProcessingTime());
                    this.state = State.PROCESSING;
                    currentItem.getItemPath().add(this.ID);
                    return currentItem.getFinishTime();
                }
                break;
            case PROCESSING:
                if (ProductionLine.config.getCurrentTime() == this.currentItem.getFinishTime()) {
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
        return ((ProductionLine.config.getM()*multiplier) +
                (ProductionLine.config.getN()*multiplier) * (ProductionLine.getRandomInst().nextDouble() - 0.5));
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
        sb.append(", isEventAvailable=").append(isEventAvailable);
        sb.append(", currentItem=").append(currentItem);
        sb.append(", state=").append(state);
        sb.append(", multiplier=").append(multiplier);
        sb.append('}');
        return sb.toString();
    }
}
