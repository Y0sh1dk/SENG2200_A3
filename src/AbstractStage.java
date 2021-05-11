import java.util.ArrayList;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected ArrayList<StageEvent> events;
    protected T currentItem;
    protected State state;


    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        this.events = new ArrayList<>();
        this.state = State.STARVED;
    }

    public AbstractStage(String inID) {
        this();
        this.ID = inID;
    }

    public AbstractStage(String inID, AbstractStage.State startingState) {
        this(inID);
        this.state = startingState;
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
