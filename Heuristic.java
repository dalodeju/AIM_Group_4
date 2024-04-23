import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Based on the Best-Fit Heuristic Developed by
 * D. Cao and V. M. Kotov, "A best-fit heuristic algorithm for two-dimensional bin packing problem,"
 * Proceedings of 2011 International Conference on Electronic & Mechanical Engineering and Information Technology, Harbin,
 * China, 2011, pp. 3789-3791, doi: 10.1109/EMEIT.2011.6023883
 */

public class Heuristic {

    public static String INPUT_FILE_PATH = "BPP.txt";

    static class Bin {
        int length;
        List<Integer> items = new ArrayList<>();

        public Bin(int length) {
            this.length = length;
        }
    }

    public static int binPacking(long[] weights, long[] nOfEach, int binLength, String testCaseName) {
        List<Integer> items = new ArrayList<>();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < nOfEach[i]; j++) {
                items.add((int) weights[i]);
            }
        }

        Collections.sort(items, Collections.reverseOrder());

        List<Bin> bins = new ArrayList<>();
        bins.add(new Bin(binLength));

        for (int item : items) {
            boolean packed = false;
            for (Bin bin : bins) {
                if (bin.length >= item) {
                    bin.items.add(item);
                    bin.length -= item;
                    packed = true;
                    break;
                }
            }
            if (!packed) {
                Bin newBin = new Bin(binLength);
                newBin.items.add(item);
                newBin.length -= item;
                bins.add(newBin);
            }
        }

        printBins(testCaseName, binLength, bins, items);

        return bins.size();
    }

    private static void printBins(String testCaseName, int binLength, List<Bin> bins, List<Integer> items) {
        System.out.println(testCaseName);
        System.out.println("Available Weights: " + items);
        System.out.println("Maximum Bin Size: " + binLength + "\n");

        for (int i = 0; i < bins.size(); i++) {
            System.out.println("Bin " + (i + 1) + ":");
            Bin bin = bins.get(i);
            System.out.println(bin.items);
        }
    }

    public static void main(String[] args) {
        File f = new File(Heuristic.INPUT_FILE_PATH);
        try {
            Scanner scanner = new Scanner(f);
            Pattern toMatchPattern = Pattern.compile("'TEST([0-9])*'");
            while (scanner.hasNext(toMatchPattern)) {
                String testCaseName = scanner.nextLine();
                int nWeights = scanner.nextInt();
                int binCap = scanner.nextInt();
                long[] weights = new long[nWeights];
                long[] nOfEach = new long[nWeights];
                List<Integer> allWeights = new ArrayList<>();
                for (int i = 0; i < nWeights; i++) {
                    weights[i] = scanner.nextLong();
                    nOfEach[i] = scanner.nextInt();
                    for (int j = 0; j < nOfEach[i]; j++) {
                        allWeights.add((int) weights[i]);
                    }
                }
                scanner.nextLine();

                int numBins = binPacking(weights, nOfEach, binCap, testCaseName);
                System.out.println("\nNumber of bins used: " + numBins + "\n");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}


