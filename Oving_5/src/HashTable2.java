import java.util.Random;
import java.util.stream.IntStream;

public class HashTable2 {

  static abstract class HashTable {
    protected int[] table;
    protected int m;
    protected int collisions;

    public HashTable(int size) {
      this.m = size;
      this.table = new int[size];
      this.collisions = 0;
      for (int i = 0; i < size; i++) {
        table[i] = -1;
      }
    }

    public abstract void insert(int key);

    public int getCollisions() {
      return collisions;
    }
  }


  static class LinearProbingHashTable extends HashTable {
    public LinearProbingHashTable(int size) {
      super(size);
    }

    @Override
    public void insert(int key) {
      int hash = (key % m + m) % m;
      while (table[hash] != -1) {
        collisions++;
        hash = (hash + 1) % m;
      }
      table[hash] = key;
    }
  }


  static class DoubleHashingHashTable extends HashTable {
    public DoubleHashingHashTable(int size) {
      super(size);
    }

    private int hash2(int key) {
      return 1 + ((key % (m - 1) + m - 1) % (m - 1));
    }

    @Override
    public void insert(int key) {
      int hash = ((key % m) + m) % m;
      int h2 = hash2(key);
      while (table[hash] != -1) {
        collisions++;
        hash = ((hash + h2) % m + m) % m;
      }
      table[hash] = key;
    }
  }

  private static int[] generateArray(int m) {
    int[] randomNumbers = new int[m];

    // Generer tall
    Random rand = new Random();
    randomNumbers[0] = rand.nextInt(1000);
    for (int i = 1; i < m; i++) {
      randomNumbers[i] = randomNumbers[i-1] + rand.nextInt(1000) + 1;
    }

    // Stokk om tallene
    IntStream.range(0, m).forEach(i -> {
      int randomIndex = rand.nextInt(m);
      int temp = randomNumbers[i];
      randomNumbers[i] = randomNumbers[randomIndex];
      randomNumbers[randomIndex] = temp;
    });

    return randomNumbers;
  }

  private static String getTimeLinearProbing(int[] randomNumbers, int m, int fillRate, int numRuns) {
    LinearProbingHashTable linearHashTable = new LinearProbingHashTable(m);
    int limit = (int) (m * fillRate / 100.0);

    long totalTime = 0;

    for (int i = 0; i < numRuns; i++) {
      long start = System.currentTimeMillis();
      for (int j = 0; j < limit; j++) {
        linearHashTable.insert(randomNumbers[j]);
      }
      long end = System.currentTimeMillis();
      totalTime += (end - start);
    }

    return "Linear probing collisions:"+ linearHashTable.getCollisions() +
      "\nAverage elapsed time in milli seconds over " + numRuns + " runs: " + (totalTime / numRuns);
  }

  private static String getTimeDoubleHash(int[] randomNumbers, int m, int fillRate, int numRuns) {
    DoubleHashingHashTable doubleHashTable = new DoubleHashingHashTable(m);
    int limit = (int) (m * fillRate / 100.0);

    long totalTime = 0;

    for (int i = 0; i < numRuns; i++) {
      long start = System.currentTimeMillis();
      for (int j = 0; j < limit; j++) {
        doubleHashTable.insert(randomNumbers[j]);
      }
      long end = System.currentTimeMillis();
      totalTime += (end - start);
    }

    return "Double hashing collisions:"+ doubleHashTable.getCollisions() +
      "\nAverage elapsed time in milli seconds over " + numRuns + " runs: " + (totalTime / numRuns);
  }


  public static void main(String[] args) {
    int m = 10_000_019;
    int[] randomNumbers = generateArray(m);
    int[] fillRates = {50, 80, 90, 99, 100};
    for (int fillRate : fillRates) {
      System.out.println("\nFor fill rate " + fillRate + "%:");
      System.out.println(getTimeLinearProbing(randomNumbers, m, fillRate, 1));
      System.out.println(getTimeDoubleHash(randomNumbers, m, fillRate, 1));
    }
  }
}
