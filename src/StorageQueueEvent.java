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

    @Override
    public int compareTo(AbstractEvent abstractEvent) {
        return 0;
    }
}
