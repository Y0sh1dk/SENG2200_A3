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

    /**
     * Constructor when no args are given
     */
    public Config() {
        this.currentTime = 0;
    }

    /**
     * parseArgs() method
     * @param args String array of args read from entrypoint method
     * @throws Exception if cannot parse args
     */
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

    /**
     * getM()
     * @return returns average processing time of a item
     */
    public int getM() {
        return M;
    }

    /**
     * getN() method
     * @return returns the range of prcessing time in a stage
     */
    public int getN() {
        return N;
    }

    /**
     * getQmax() method
     * @return returns the max size that storage queues are allowed
     */
    public int getQmax() {
        return Qmax;
    }

    /**
     * getCurrentTime() method
     * @return the current time of the simulation
     */
    public double getCurrentTime() {
        return currentTime;
    }

    /**
     * setCurrentTime() method
     * @param inTime double to sest as current time
     */
    public void setCurrentTime(double inTime) {
        this.currentTime = inTime;
    }

    /**
     * getNumGenSeed() method
     * @return seed for the random number generator
     */
    public Integer getNumGenSeed() {
        return NUM_GEN_SEED;
    }

    /**
     * getMaxRunTime() method
     * @return the max runtime of the simulation
     */
    public double getMaxRunTime() {
        return MAX_RUN_TIME;
    }

    /**
     * toString() method
     * @return String representation of class
     */
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
