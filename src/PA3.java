
/**
 * FileName: PA3.java
 * Assessment: SENG2200 - A3
 * Author: Yosiah de Koeyer
 * Student No: c3329520
 * <p>
 * Description:
 * Entrypoint class for PA2
 */
public class PA3 {

    public static void main(String[] args) {
        Config config = new Config();       // Config if simulation

        if (args.length != 3) {             // If wrong amount of args given
            System.out.println("Invalid Arguments, Example: java PA3 1000 1000 7");
            System.exit(1);
        }

        try {
            config.parseArgs(args);
        } catch (Exception e) {
            System.exit(1);
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
