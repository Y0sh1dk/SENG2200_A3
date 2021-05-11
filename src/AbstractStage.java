import java.util.ArrayList;

public abstract class AbstractStage<T extends Item> {
    private String ID;
    protected ArrayList<StageEvent> events;
    protected T currentItem;
    protected State state;

    protected StorageQueue<T> prevQueue;
    protected StorageQueue<T> nextQueue;

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

    /**
     * Push item into next queue
     */
    protected void pushItem() {
        this.nextQueue.add(this.currentItem);
        this.currentItem = null;
    }

    /**
     * get item from prev queue if item available
     * @return true if successful, else false
     */
    protected boolean getItem() {
        if (this.prevQueue.size() != 0) {
            this.currentItem = prevQueue.remove();
            return true;
        }
        return false;
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

    abstract public StageEvent process();
}
