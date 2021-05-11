public class BeginStage<T extends Item> extends AbstractStage<T> {
    private StorageQueue<T> nextQueue;

    public BeginStage(String inID) {
        super(inID, State.PROCESSING);
        this.nextQueue = new StorageQueue<>();
    }

    @Override
    public StageEvent process() {
        return null;
    }
}
