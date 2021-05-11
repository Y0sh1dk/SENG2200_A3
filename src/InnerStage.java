public class InnerStage<T extends Item> extends AbstractStage<T> {


    public InnerStage(String inID) {
        super(inID);
        this.prevQueue = new StorageQueue<>();
        this.nextQueue = new StorageQueue<>();
    }

    @Override
    public StageEvent process() {
        return null;
    }
}
