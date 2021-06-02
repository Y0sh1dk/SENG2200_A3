/**
 * FileName: Config.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Config class
 */

public class Config {
    private final static double MAX_RUN_TIME = 10000000;    // 7 zeros
    private final static Integer NUM_GEN_SEED = null;       // Random number generator seed
    private int M;                                          // Average proceessing time of item in a stage
    private int N;                                          // Range of processing time in a stage
    private int Qmax;                                       // Size of storage queues
    private double currentTime;                             // Current simulation time

    public Config() {
        this.currentTime = 0;
    }

    public void parseArgs(String[] args) throws Exception {
        try {
            this.M = Integer.parseInt(args[0]);
            this.N = Integer.parseInt(args[1]);
            this.Qmax = Integer.parseInt(args[2]);
        } catch (Exception e) {
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

    public Integer getNumGenSeed() {
        return NUM_GEN_SEED;
    }

    public double getMaxRunTime() {
        return MAX_RUN_TIME;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Config{");
        sb.append("M=").append(M);
        sb.append(", N=").append(N);
        sb.append(", Qmax=").append(Qmax);
        sb.append(", currentTime=").append(currentTime);
        sb.append('}');
        return sb.toString();
    }
}
