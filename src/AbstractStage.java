import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected ArrayList<StageEvent> events;
    protected T currentItem;
    protected State state;
    //protected ProductionLine<T> productionLine; // production line this stage is apart of

    public ArrayList<StageEvent> getEvents() {
        return this.events;
    }

    public void clearEvents() {
        this.events.clear();
    }


    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        this.events = new ArrayList<>();
        this.state = State.STARVED;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState) {
        this();
        this.ID = inID;
        this.state = inStartingState;
    }

    public abstract void process();


    //TODO(yoshi): needs to be different for double stages?
    protected StageEvent genEvent() {
        StageEvent event = new StageEvent();
        event.setStageID(this.ID);
        event.setStartTime(ProductionLine.config.getCurrentTime());
        event.setFinishTime(event.getStartTime() + this.calProcessingTime());
        return event;
    }

    private double calProcessingTime() {
        Random r = new Random(ProductionLine.config.getNumGenSeed());
        return ProductionLine.config.getM() + ProductionLine.config.getN() * (r.nextDouble() - 0.5);
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
}
