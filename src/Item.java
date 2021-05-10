import java.util.ArrayList;

public class Item {
    private String uniqueID;
    private ArrayList<ItemEvent> events;


    private Item() { // needs to have a ID so private
        this.events = new ArrayList<>();
    }

    public Item(String inID) {
        this();
        this.uniqueID = inID;
    }
}
