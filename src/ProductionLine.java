import java.util.ArrayList;

public class ProductionLine {
    private BeginStage beginStage;
    private FinalStage finalStage;
    private ArrayList<InnerStage> innerStages;


    public ProductionLine() {
        this.beginStage = null;
        this.finalStage = null;
        this.innerStages = new ArrayList<>();
    }


}
