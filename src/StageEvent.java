import java.util.Comparator;

public class StageEvent {
    private String stageID;
    private String itemID;
    private double startTime;
    private double finishTime;
    private boolean isFinished;

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

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public static class finishTimeComparator implements Comparator<StageEvent> {
        @Override
        public int compare(StageEvent o1, StageEvent o2) {
            return Double.compare(o1.getFinishTime(), o2.getFinishTime());
        }
    }
}


