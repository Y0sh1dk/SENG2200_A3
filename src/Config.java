public class Config {
    private final static double MAX_RUN_TIME = 10000000;
    private final static int NUM_GEN_SEED = 3453;

    private int M;
    private int N;
    private int Qmax;

    private double currentTime;

    public Config() {
        this.currentTime = 0;
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


    public double getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(double inTime) {
        this.currentTime = inTime;
    }

    public int getNumGenSeed() {
        return NUM_GEN_SEED;
    }

    public double getMaxRunTime() {
        return MAX_RUN_TIME;
    }
}
