import java.util.ArrayList;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected ArrayList<StageEvent> events;
    protected T currentItem;
    protected State state;
    protected ProductionLine<T> productionLine; // production line this stage is apart of


    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        this.events = new ArrayList<>();
        this.state = State.STARVED;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState, ProductionLine<T> inPL) {
        this();
        this.ID = inID;
        this.state = inStartingState;
        this.productionLine = inPL;
    }

    /**
     * Sent events back to the production line
     * @param inEvent
     */
    protected void SendEvent(StageEvent inEvent) {
        this.productionLine.AddEvent(inEvent);
    }



    //private void updateState() {
    //    // Processing
    //    if (this.currentItem != null && this.currentItem.getState() != Item.State.FINISHED) {
    //        this.state = State.PROCESSING;
    //    }
    //    // Blocked
    //    else if (this.currentItem != null && this.currentItem.getState() == Item.State.FINISHED) {
    //        this.state = State.BLOCKED;
    //    }
    //    // Starved
    //    else if (this.currentItem == null && this.prevQueue.size() == 0) {
    //        this.state = State.STARVED;
    //    }
    //}
    
    
    
    

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return this.state;
    }

    public String getID() {
        return this.ID;
    }

    public StageEvent process() {
        // Currently doesnt have an item
        if (currentItem == null) {

        }
        return null;
    }
}
