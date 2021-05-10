public class PA3 {
    public static Config config;

    public static void main(String[] args) {
        config = new Config();

        if (args.length <= 3) { // If wrong amount of args given
            System.out.println("Invalid Arguments, Example: A3 30 3 Process1.txt Process2.txt Process3.txt");
            System.exit(1); // TODO(yoshi) : what code?
        }

        try {
            config.parseArgs(args);
        } catch (Exception e) {
            System.exit(1); // TODO(yoshi): what code?
        }

        PA3 PA3 = new PA3();
        PA3.run(config);
    }

    private void run(Config config) {

    }
}
