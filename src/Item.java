import java.util.ArrayList;

public class Item {
    private String uniqueID;
    private ArrayList<ItemEvent> events;
    private State state;

    public String getUniqueID() {
        return uniqueID;
    }


    enum State{
        PROCESSING,
        QUEUED,
        FINISHED,
    }

    private Item() {                            // needs to have a ID so private
        this.events = new ArrayList<>();
        this.state = State.QUEUED;             // starts queued
    }

    public Item(String inID) {
        this();
        this.uniqueID = inID;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State inState) {
        this.state = inState;
    }
}
