import java.util.ArrayList;

/**
 * FileName: Item.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * A class to represent an Item being produced in the production line
 */
public class Item {
    private String uniqueID;
    private ArrayList<String> itemPath;
    private State state;

    private Double finishTime;

    public String getUniqueID() {
        return uniqueID;
    }

    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }


    enum State{
        PROCESSING,
        QUEUED,
        FINISHED,
    }

    private Item() {                            // needs to have a ID so private
        this.itemPath = new ArrayList<>();
        this.state = State.QUEUED;             // starts queued
    }

    public Item(String inID) {
        this();
        this.uniqueID = inID;
    }

    public ArrayList<String> getItemPath() {
        return itemPath;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State inState) {
        this.state = inState;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("uniqueID='").append(uniqueID).append('\'');
        sb.append(", events=").append(itemPath);
        sb.append(", state=").append(state);
        sb.append(", finishTime=").append(finishTime);
        sb.append('}');
        return sb.toString();
    }
}
