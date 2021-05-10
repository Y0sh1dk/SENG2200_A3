import java.util.ArrayList;

public abstract class AbstractStage<T> {
    private String ID;
    private ArrayList<StageEvent> events;

    private AbstractStage() {
        this.events = new ArrayList<>();
    }

    public AbstractStage(String inID) {
        this();
        this.ID = inID;
    }



}
