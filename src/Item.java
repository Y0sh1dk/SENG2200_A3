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

    public String getUniqueID() {
        return uniqueID;
    }

    public Double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Double finishTime) {
        this.finishTime = finishTime;
    }

    private Item() {                            // needs to have a ID so private
        this.itemPath = new ArrayList<>();
    }

    public Item(String inID) {
        this();
        this.uniqueID = inID;
    }

    public ArrayList<String> getItemPath() {
        return itemPath;
    }

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
