import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class FFD {

    public static String INPUT_FILE_PATH = "BPP.txt";

    public static void main(String[] args) {
        File f = new File(Main.INPUT_FILE_PATH);
        try {
            Scanner scanner = new Scanner(f);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.startsWith("'TEST")) {
                    String testCaseName = line;
                    int nWeights = scanner.nextInt();
                    int binCap = scanner.nextInt();
                    long[] weights = new long[nWeights];
                    long[] nOfEach = new long[nWeights];
                    for (int i = 0; i < nWeights; i++) {
                        weights[i] = scanner.nextLong();
                        nOfEach[i] = scanner.nextInt();
                    }
                    System.out.printf("weights = %s\n", Arrays.toString(weights));
                    System.out.printf("nOfEachWeight = %s\n", Arrays.toString(nOfEach));
                    scanner.nextLine(); // Consume newline character

                    Solver solver = new Solver(testCaseName, binCap, weights, nOfEach);
                    solver.solve();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static class Solver {
        long[] weights, nOfEach;
        int binCap;
        String testCaseName;

        Solver(String testCaseName, int binCap, long[] weights, long[] nOfEach) {
            this.testCaseName = testCaseName;
            this.binCap = binCap;
            this.weights = weights;
            this.nOfEach = nOfEach;
        }

        public void solve() {
            List<Integer> items = new ArrayList<>();
            for (int i = 0; i < weights.length; i++) {
                for (int j = 0; j < nOfEach[i]; j++) {
                    items.add((int) weights[i]);
                }
            }

            // Convert List to array
            int[] itemWeights = items.stream().mapToInt(i -> i).toArray();

            // Call the First Fit function and print the result
            List<List<Integer>> bins = firstFit(itemWeights, binCap);
            System.out.println(testCaseName + " requires " + bins.size() + " bins.");
            System.out.println("Bins: ");
            for (int i = 0; i < bins.size(); i++) {
                System.out.println("Bin " + (i + 1) + ": " + bins.get(i));
            }
        }

        public static List<List<Integer>> firstFit(int[] itemWeights, int capacity) {
            List<List<Integer>> bins = new ArrayList<>();
            List<Integer> binRemainders = new ArrayList<>();
            int binCount = 0;

            for (int weight : itemWeights) {
                boolean placed = false;

                for (int i = 0; i < binCount; i++) {
                    if (binRemainders.get(i) >= weight) {
                        binRemainders.set(i, binRemainders.get(i) - weight);
                        bins.get(i).add(weight);
                        placed = true;
                        break;
                    }
                }

                if (!placed) {
                    binRemainders.add(capacity - weight);
                    bins.add(new ArrayList<>(Arrays.asList(weight)));
                    binCount++;
                }
            }

            return bins;
        }
    }
}
