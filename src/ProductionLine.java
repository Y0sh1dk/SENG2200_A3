import java.util.*;

public class ProductionLine<T extends Item> {
    private String beginStageID;
    private String finalStageID;
    private final HashMap<String ,AbstractStage<T>> stages;
    private final HashMap<String ,StorageQueue<T>> storageQueues;
    private final ArrayList<Double> pendingFinishTimes;

    private static Random randomInst = null;
    public static Config config;

    private ProductionLine() {
        this.stages = new HashMap<>();
        this.storageQueues = new HashMap<>();
        this.pendingFinishTimes = new ArrayList<>();
    }

    public ProductionLine(Config inConfig) {
        this();
        config = inConfig;
        randomInst = new Random(config.getNumGenSeed());
        this.generateProductionLine();
    }

    public void run() {
        while(config.getCurrentTime() < config.getMaxRunTime()) {
            boolean eventOccurred = true;
            while(eventOccurred) {
                eventOccurred = false;

                for (AbstractStage<T> s : this.stages.values()) {
                    Double finishTime = s.process();
                    if (finishTime != 0) {
                        eventOccurred = true;
                        this.pendingFinishTimes.add(finishTime);
                    }
                }
            }
            this.clearFinishedTimes();
            Collections.sort(this.pendingFinishTimes);
            config.setCurrentTime(pendingFinishTimes.get(0));
        }
    }

    private void clearFinishedTimes() {
        this.pendingFinishTimes.removeIf((aDouble) -> aDouble <= config.getCurrentTime());
    }


    public void generateProductionLine() {
        String[] stageNames = {"S0", "S1", "S2a", "S2b", "S3", "S4a", "S4b", "S5"};

        // ---------- STAGES ----------
        this.beginStageID = stageNames[0];
        this.finalStageID = stageNames[stageNames.length - 1];

        // all stages
        for (String s : stageNames) {
            if (s.equalsIgnoreCase(this.beginStageID)) {
                this.stages.put(s, new BeginStage<>(s));
            } else if (s.equalsIgnoreCase((this.finalStageID))) {
                this.stages.put(s, new FinalStage<>(s));
            } else {
                this.stages.put(s, new InnerStage<>(s));
            }
        }

        // ---------- StorageQueues ----------
        // -- Begin stage --
        this.getBeginStage().setNextQueue(this.genStorageQueue("Q01"));

        // -- Inner stages --
        // S1
        // Prev
        this.getInnerStage("S1").setPrevQueue(this.genStorageQueue("Q01"));
        // Next
        this.getInnerStage("S1").setNextQueue(this.genStorageQueue("Q12"));

        // S2a
        this.getInnerStage("S2a").setMultiplier(2);
        // Prev
        this.getInnerStage("S2a").setPrevQueue(this.genStorageQueue("Q12"));
        // Next
        this.getInnerStage("S2a").setNextQueue(this.genStorageQueue("Q23"));

        // S2b
        this.getInnerStage("S2b").setMultiplier(2);
        // Prev
        this.getInnerStage("S2b").setPrevQueue(this.genStorageQueue("Q12"));
        // Next
        this.getInnerStage("S2b").setNextQueue(this.genStorageQueue("Q23"));

        // S3
        // Prev
        this.getInnerStage("S3").setPrevQueue(this.genStorageQueue("Q23"));
        // Next
        this.getInnerStage("S3").setNextQueue(this.genStorageQueue("Q34"));

        // S4a
        this.getInnerStage("S4a").setMultiplier(2);
        // Prev
        this.getInnerStage("S4a").setPrevQueue(this.genStorageQueue("Q34"));
        // Next
        this.getInnerStage("S4a").setNextQueue(this.genStorageQueue("Q45"));

        // S4b
        this.getInnerStage("S4b").setMultiplier(2);
        // Prev
        this.getInnerStage("S4b").setPrevQueue(this.genStorageQueue("Q34"));
        // Next
        this.getInnerStage("S4b").setNextQueue(this.genStorageQueue("Q45"));

        // -- Final stage --
        this.getFinalStage().setPrevQueue(genStorageQueue("Q45"));
    }


    private BeginStage<T> getBeginStage() {
        return (BeginStage<T>) this.stages.get(this.beginStageID);
    }

    private InnerStage<T> getInnerStage(String inStageID) {
        return (InnerStage<T>) this.stages.get(inStageID);
    }

    private FinalStage<T> getFinalStage() {
        return (FinalStage<T>) this.stages.get(this.finalStageID);
    }

    private StorageQueue<T> genStorageQueue(String inQueueID) {
        this.storageQueues.putIfAbsent(inQueueID, new StorageQueue<>(inQueueID, config.getQmax()));
        return this.storageQueues.get(inQueueID);
    }

    // TODO(yoshi): this
    public String report() {
        StringBuilder sb = new StringBuilder();
        sb.append(config);
        sb.append("\n");
        sb.append(this.prodStationsReport());
        sb.append("\n");
        sb.append(this.storageQueuesReport());
        sb.append("\n");
        sb.append(this.prodPathsReport());

        //this.stages.forEach((v, k) -> {
        //    sb.append(v).append(": ").append(k.numProcessed).append("\n");
        //});
        //sb.append("\n\n");
        //this.storageQueues.forEach((v, k) -> {
        //    k.getEvents().forEach((storageQueueEvent -> {
        //        sb.append(storageQueueEvent).append("\n");
        //    }));
        //});
        return sb.toString();
    }

    private String prodStationsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production Stations:\n");
        sb.append("--------------------------------------------");

        return sb.toString();
    }

    private String storageQueuesReport() {

        HashMap<String, Double> averageQueueTimes = new HashMap<>();

        // For each queue
        for (StorageQueue<T> queue : this.storageQueues.values()) {

            // ArrayList of times items spent in the queue
            ArrayList<Double> inQueueTimes = new ArrayList<>();

            // Get HashMap of events
            HashMap<String, ArrayList<StorageQueueEvent>> allItemEvents = queue.getEvents();

            // Remove "" events (generated when stage tries to get item from empty queue
            allItemEvents.remove("");

            // For all the events for each item in that queue
            for (ArrayList<StorageQueueEvent> itemEvents : allItemEvents.values()) {

                // Sort events by time for that itemID
                itemEvents.sort(Comparator.comparing(StorageQueueEvent::getTime));

                // For each event for specific item
                Double inTime = null;
                Double outTime = null;
                for (StorageQueueEvent itemEvent : itemEvents) {
                    // If added
                    if (itemEvent.getType() == StorageQueueEvent.Type.ADD) {
                        inTime = itemEvent.getTime();
                    }
                    // If removed
                    if (itemEvent.getType() == StorageQueueEvent.Type.REMOVE) {
                        outTime = itemEvent.getTime();
                    }
                }
                // If item entered AND exited
                if (inTime != null && outTime != null) {
                    inQueueTimes.add(outTime - inTime);
                }
            }
            double average = inQueueTimes.stream().mapToDouble(Double::doubleValue).sum() / inQueueTimes.size();
            averageQueueTimes.put(queue.getID(), average);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Storage Queues:\n");
        sb.append("------------------------------\n");
        sb.append(String.format("%-9s %-22s %-9s", "Store", "AvgTime[t]", "AvgItems\n"));
        for(Map.Entry<String, Double> entry : averageQueueTimes.entrySet()) {
            sb.append(String.format("%-9s %-22s %-9s\n", entry.getKey(), entry.getValue(), "brr"));
        }

        return sb.toString();
    }

    private String prodPathsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production Paths:\n");
        sb.append("------------------");

        return sb.toString();
    }

    public static Random getRandomInst() {
        return randomInst;
    }
}
