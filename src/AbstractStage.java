import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected int numProcessed;
    protected boolean isEventAvailable;
    protected T currentItem;
    protected State state;

    protected double multiplier;

    public void setMultiplier(double inMulti) {
        this.multiplier = inMulti;
    }



    enum State {
        PROCESSING,
        BLOCKED,
        STARVED,
    }

    private AbstractStage() {
        this.state = State.STARVED;
        this.isEventAvailable = false;
        this.multiplier = 1;
    }


    public AbstractStage(String inID, AbstractStage.State inStartingState) {
        this();
        this.ID = inID;
        this.state = inStartingState;
    }

    public abstract Double process();


    protected double calProcessingTime() {
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
