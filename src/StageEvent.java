public class StageEvent extends AbstractEvent {
    private String stageID;
    private double startTime;
    private double finishTime;

    public StageEvent() {

    }

    public double getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public String getStageID() {
        return stageID;
    }

    public void setStageID(String stageID) {
        this.stageID = stageID;
    }

    @Override
    public int compareTo(AbstractEvent abstractEvent) {
        return 0;
    }
}
