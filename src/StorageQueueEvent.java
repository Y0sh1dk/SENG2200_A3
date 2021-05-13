public class StorageQueueEvent extends AbstractEvent{
    private String storageQueueID;
    private String itemID;
    private Type type;
    private double time;

    enum Type {
        ADD,
        REMOVE,
        ADD_FAILED,
        REMOVE_FAILED,
    }

    public StorageQueueEvent(String inStorageQueueID, String inItemID, Double inTime, Type inType) {
        this.storageQueueID = inStorageQueueID;
        this.itemID = inItemID;
        this.time = inTime;
        this.type = inType;
    }

    public String getID() {
        return this.storageQueueID;
    }

    public String getItemID() {
        return this.itemID;
    }

    public Type getType() {
        return this.type;
    }

    public double getTime() {
        return time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StorageQueueEvent{");
        sb.append("storageQueueID='").append(storageQueueID).append('\'');
        sb.append(", itemID='").append(itemID).append('\'');
        sb.append(", type=").append(type);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(AbstractEvent abstractEvent) {
        return 0;
    }
}
