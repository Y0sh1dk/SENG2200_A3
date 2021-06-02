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
    private String uniqueID;                    // ID of item
    private ArrayList<String> itemPath;         // Stages that the item has passed through
    private Double finishTime;                  // Time that the item finishes processing (should probs be in stage)

    /**
     * getUniqueID() method
     * @return the ID of the item
     */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * getFinishTime() method
     * @return the upcoming finish time of the items processing
     */
    public Double getFinishTime() {
        return finishTime;
    }

    /**
     * getFinishTime()
     * @param finishTime Double to set as finish time
     */
    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * Constructor, private as it should not be able to be instantiated without an ID
     */
    private Item() {
        this.itemPath = new ArrayList<>();
    }

    /**
     * Constructor when ID is given
     * @param inID ID of item
     */
    public Item(String inID) {
        this();
        this.uniqueID = inID;
    }

    /**
     * getItemPath() method
     * @return ArrayList containing the path the item took
     */
    public ArrayList<String> getItemPath() {
        return itemPath;
    }

    /**
     * toString() method
     * @return String representation of class
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Item{");
        sb.append("uniqueID='").append(uniqueID).append('\'');
        sb.append(", itemPath=").append(itemPath);
        sb.append(", finishTime=").append(finishTime);
        sb.append('}');
        return sb.toString();
    }
}
