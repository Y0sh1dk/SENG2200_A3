public class Config {
    private final static double MAX_RUN_TIME = 10000000;
    private final static int NUM_GEN_SEED = 1;

    private int M;
    private int N;
    private int Qmax;

    private double currentTime;
    private double previousTime;

    public Config() {
        this.currentTime = 0;
        this.previousTime = 0;
    }

    public void parseArgs(String[] args) throws Exception {
        try {
            this.M = Integer.parseInt(args[0]);
            this.N = Integer.parseInt(args[1]);
            this.Qmax = Integer.parseInt(args[2]);
        } catch (Exception e) {
            // TODO(yoshi): Custom exception
            System.err.println(e);
            throw new Exception("Error reading args");
        }
    }

    public int getM() {
        return M;
    }

    public int getN() {
        return N;
    }

    public int getQmax() {
        return Qmax;
    }

    public boolean incrementCurrentTime(double inTime) {
        if (this.currentTime + inTime > MAX_RUN_TIME) {
            return false;
        }
        this.previousTime = this.currentTime;
        this.currentTime += inTime;
        return true;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public double getPreviousTime() {
        return previousTime;
    }
}
