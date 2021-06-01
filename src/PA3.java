
public class PA3 {

    public static void main(String[] args) {
        Config config = new Config();

        if (args.length != 3) { // If wrong amount of args given
            System.out.println("Invalid Arguments, Example: java PA3 1000 1000 7");
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
        ProductionLine<Item> pl = new ProductionLine<>(config);
        pl.run();
        System.out.println(pl.report());

    }
}
