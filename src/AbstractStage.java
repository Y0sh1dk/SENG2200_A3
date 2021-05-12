import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected StageEvent currentEvent;
    protected boolean isEventAvailable;
    protected T currentItem;
    protected State state;

    protected double multiplier;

    public int numProcessed;

    public void setMultiplier(double inMulti) {
        this.multiplier = inMulti;
    }



    public StageEvent getEvent() {
        if (this.isEventAvailable) {
            this.isEventAvailable = false;
            return this.currentEvent;
        }
        return null;
    }

    public boolean isEventAvailable() {
        return this.isEventAvailable;
    }


    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        //this.events = new ArrayList<>();
        this.state = State.STARVED;
        this.isEventAvailable = false;
        this.multiplier = 1;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState) {
        this();
        this.ID = inID;
        this.state = inStartingState;
    }

    public abstract void process();


    //TODO(yoshi): needs to be different for double stages?
    protected StageEvent genEvent() {
        this.isEventAvailable = true;
        StageEvent event = new StageEvent();
        event.setStageID(this.ID);
        event.setStartTime(ProductionLine.config.getCurrentTime());
        event.setFinishTime(event.getStartTime() + this.calProcessingTime());
        event.setItemID(currentItem.getUniqueID());
        return event;
    }

    private double calProcessingTime() {
        //Random r = new Random(ProductionLine.config.getNumGenSeed());
        double d = ((ProductionLine.config.getM()*multiplier) +
                (ProductionLine.config.getN()*multiplier) * (ProductionLine.r.nextDouble() - 0.5));
        //System.out.println(d);
        return d;
    }



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
