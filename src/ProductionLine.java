import java.util.*;

/**
 * FileName: ProductionLine.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Class to represent a production line, stores stages and the queues between them, run() method starts the DES
 */
public class ProductionLine<T extends Item> {
    private String beginStageID;                                    // ID of starting stage
    private String finalStageID;                                    // ID of final stage
    private final HashMap<String ,AbstractStage<T>> stages;         // Hashmap mapping ID's to stages
    private final HashMap<String ,StorageQueue<T>> storageQueues;   // Hashmap mapping ID's  to storage queues
    private final ArrayList<Double> pendingFinishTimes;             // Current finishing times of items in stages
    private static Random randomInst = null;                        // Instance of `Random` class
    private static Config config;                                   // Config instance

    private ProductionLine() {
        this.stages = new HashMap<>();
        this.storageQueues = new HashMap<>();
        this.pendingFinishTimes = new ArrayList<>();
    }

    public ProductionLine(Config inConfig) {
        this();
        config = inConfig;
        ProductionLine.randomInst = config.getNumGenSeed() != null ? new Random(config.getNumGenSeed()) : new Random();
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

    public String report() {
        StringBuilder sb = new StringBuilder();
        sb.append(config);
        sb.append("\n");
        sb.append("\n");
        sb.append(this.summaryReport());
        sb.append("\n");
        sb.append(this.prodStationsReport());
        sb.append("\n");
        sb.append(this.storageQueuesReport());
        sb.append("\n");
        sb.append(this.prodPathsReport());
        return sb.toString();
    }

    private String summaryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total items produced: ").append(this.getFinalStage().getWarehouse().size());
        return sb.toString();
    }

    private String prodStationsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Production Stations:\n");
        sb.append("--------------------------------------------------------\n");
        sb.append(String.format("%-15s %-15s %-15s %-15s", "Stage:", "Work[%]", "Starve[t]", "Block[t]")).append(System.lineSeparator());

        // Should have used TreeMap to keep alphabetical order :(
        String[] stagesIDArray = this.stages.keySet().toArray(new String[0]);
        Arrays.sort(stagesIDArray);

        // For stages in alphabetical order
        for (String stageID : stagesIDArray) {
            // Get stage
            AbstractStage<T> stage = this.stages.get(stageID);
            // calculate work
            double timeNotWorking = stage.getTotalBlockedTime() + stage.getTotalStarvedTime();
            double percentWorking = 100 * ((ProductionLine.config.getMaxRunTime() - timeNotWorking) /
                    ProductionLine.config.getMaxRunTime());

            sb.append(String.format(
                    "%-15s %-15s %-15s %-15s",
                    stage.getID(),
                    String.format("%.2f", percentWorking),
                    String.format("%.2f" ,stage.getTotalStarvedTime()),
                    String.format("%.2f", stage.getTotalBlockedTime()))).append("\n");
        }
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
        sb.append("-----------------------------------------\n");
        sb.append(String.format("%-12s %-12s %-12s", "Store:", "AvgTime[t]", "AvgItems")).append(System.lineSeparator());
        for(Map.Entry<String, Double> entry : averageQueueTimes.entrySet()) {
            sb.append(String.format(
                    "%-12s %-12s %-12s\n",
                    entry.getKey(),
                    String.format("%.2f", entry.getValue()),
                    String.format("%.2f", this.storageQueues.get(entry.getKey()).getAverageItems() /
                            ProductionLine.config.getMaxRunTime())));
        }

        return sb.toString();
    }

    private String prodPathsReport() {
        // ONLY CONSIDERS ITEMS THAT HAVE FINISHED PRODUCTION!!!
        ArrayList<T> finishedItems = this.getFinalStage().getWarehouse();

        HashMap<String, Integer> itemPathCounts = new HashMap<>();
        itemPathCounts.put("S2a -> S4a", 0);
        itemPathCounts.put("S2a -> S4b", 0);
        itemPathCounts.put("S2b -> S4a", 0);
        itemPathCounts.put("S2b -> S4b", 0);

        // For all finished items
        for (T item : finishedItems) {
            // For all possible paths
            for (String path : itemPathCounts.keySet()) {
                // If contains both stages for that path
                if (item.getItemPath().containsAll(Arrays.asList(path.split(" -> ", 2)))) {
                    // Increment counter
                    itemPathCounts.put(path, itemPathCounts.get(path) + 1);
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Production Paths:\n");
        sb.append("------------------\n");
        for (Map.Entry<String, Integer> entry: itemPathCounts.entrySet()) {
            sb.append(entry.getKey()).append(":  ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }

    public static Config getConfig() {
        return config;
    }

    public static Random getRandomInst() {
        return randomInst;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductionLine{");
        sb.append("beginStageID='").append(beginStageID).append('\'');
        sb.append(", finalStageID='").append(finalStageID).append('\'');
        sb.append(", stages=").append(stages);
        sb.append(", storageQueues=").append(storageQueues);
        sb.append(", pendingFinishTimes=").append(pendingFinishTimes);
        sb.append('}');
        return sb.toString();
    }
}
