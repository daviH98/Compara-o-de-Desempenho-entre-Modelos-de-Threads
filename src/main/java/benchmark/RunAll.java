package benchmark;

import java.util.Arrays;

public class RunAll {

    public static void main(String[] args) throws Exception {
        int[] counts = {10, 100, 500, 1000};
        String poolSizeArg = "cores";
        int iterations = 20_000;
        int sleepMillis = 2;

        for (String a : args) {
            if (a.startsWith("--counts=")) {
                String v = a.substring("--counts=".length());
                counts = Arrays.stream(v.split(",")).mapToInt(Integer::parseInt).toArray();
            } else if (a.startsWith("--poolSize=")) {
                poolSizeArg = a.substring("--poolSize=".length());
            } else if (a.startsWith("--iterations=")) {
                iterations = Integer.parseInt(a.substring("--iterations=".length()));
            } else if (a.startsWith("--sleepMillis=")) {
                sleepMillis = Integer.parseInt(a.substring("--sleepMillis=".length()));
            }
        }

        int cores = Runtime.getRuntime().availableProcessors();
        int poolSize = poolSizeArg.equalsIgnoreCase("cores") ? cores : Integer.parseInt(poolSizeArg);

        System.out.println("model,threads,poolSize,iterations,sleepMillis,totalMillis");

        for (int n : counts) {
            long nmMillis = NMModel.run(n, poolSize, iterations, sleepMillis);
            System.out.printf("N:M,%d,%d,%d,%d,%d%n", n, poolSize, iterations, sleepMillis, nmMillis);

            long oneToOneMillis = OneToOneModel.run(n, iterations, sleepMillis);
            System.out.printf("1:1,%d,%s,%d,%d,%d%n", n, "-", iterations, sleepMillis, oneToOneMillis);
        }
    }
}
