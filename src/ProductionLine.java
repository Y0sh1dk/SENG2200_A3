import java.util.*;

public class ProductionLine<T extends Item> {
    private String beginStageID;
    private String finalStageID;
    private HashMap<String ,AbstractStage<T>> stages;
    private HashMap<String ,StorageQueue<T>> storageQueues;
    private ArrayList<StageEvent> pendingStageEvents;
    private ArrayList<StageEvent> finishedStageEvents;




    public static Random r = new Random(1);




    public static Config config;

    private ProductionLine() {
        this.stages = new HashMap<>();
        this.storageQueues = new HashMap<>();
        this.pendingStageEvents = new ArrayList<>();
        this.finishedStageEvents = new ArrayList<>();
    }

    public ProductionLine(Config inConfig) {
        this();
        config = inConfig;
        this.generateProductionLine();
    }

    public void run() {
        while(config.getCurrentTime() < config.getMaxRunTime()) {
            System.out.println(config.getCurrentTime());
            this.stages.forEach((k,v) -> {
                v.process();
            });
            this.moveFinishedEvents();
            this.getEvents();
            pendingStageEvents.sort(new StageEvent.finishTimeComparator());
            try {
                config.setCurrentTime(pendingStageEvents.get(0).getFinishTime());
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        this.stages.forEach((v, k) -> {
            System.out.println(v + ": " + k.numProcessed);
        });
    }


    private void moveFinishedEvents() {
        ListIterator<StageEvent> iter = this.pendingStageEvents.listIterator();
        while(iter.hasNext()) {
            StageEvent stageEvent = iter.next();
            if(stageEvent.isFinished()) {
                this.finishedStageEvents.add(stageEvent);
                iter.remove();
            }
        }


        //this.pendingStageEvents.forEach((stageEvent ->  {
        //    if(stageEvent.isFinished()) {
        //        this.finishedStageEvents.add(stageEvent);
        //        pendingStageEvents.remove(stageEvent);
        //    }
        //}));
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
        return "";
    }

    private void getEvents() {
        // for each stage
        this.stages.forEach((k,v) -> {
            // for each event in single stage
            if (v.isEventAvailable()) {
                this.pendingStageEvents.add(v.getEvent());
            }

            //TODO(yoshi): look at this
            //v.clearEvents();
        });
    }
}
