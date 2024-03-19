import java.util.Arrays;
import java.util.Random;

public class Oving_3 {
  public static void main(String[] args) {
    int[] table1WithDuplicates1_000_000 = generateDataWithDuplicates(1_000_000);
    int[] table2WithDuplicates10_000_000 = generateDataWithDuplicates(10_000_000);

    int[] table3WithDuplicates1_000_000 = generateDataWithDuplicates(1_000_000);
    int[] table4WithDuplicates10_000_000 = generateDataWithDuplicates(10_000_000);

    int[] table5WithoutDuplicates1_000_000 = generateDataWithoutDuplicates(1_000_000);
    int[] table6WithoutDuplicates10_000_000 = generateDataWithoutDuplicates(10_000_000);

    int[] table7WithoutDuplicates1_000_000 = generateDataWithoutDuplicates(1_000_000);
    int[] table8WithoutDuplicates10_000_000 = generateDataWithoutDuplicates(10_000_000);

    System.out.println("\nTid quicksort uten forbedring, på tabell med mange duplikater:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksort(table1WithDuplicates1_000_000, 10) + ",     " + test1(table1WithDuplicates1_000_000) + " " + test2WithDuplicates(table1WithDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksort(table2WithDuplicates10_000_000, 10) + ",     " + test1(table2WithDuplicates10_000_000) + " " + test2WithDuplicates(table2WithDuplicates10_000_000));

    System.out.println("\nTid quicksort med forbedring, på tabell med mange duplikater:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksortMedForbedring(table3WithDuplicates1_000_000, 10) + ",     " + test1(table3WithDuplicates1_000_000) + " " + test2WithDuplicates(table3WithDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksortMedForbedring(table4WithDuplicates10_000_000, 10) + ",     " + test1(table4WithDuplicates10_000_000) + " " + test2WithDuplicates(table4WithDuplicates10_000_000));

    System.out.println("\nTid quicksort uten forbedring, på tabell uten duplikater:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksort(table5WithoutDuplicates1_000_000, 10) + ",     " + test1(table5WithoutDuplicates1_000_000) + " " + test2WithoutDuplicates(table5WithoutDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksort(table6WithoutDuplicates10_000_000, 10) + ",     " + test1(table6WithoutDuplicates10_000_000) + " " + test2WithoutDuplicates(table6WithoutDuplicates10_000_000));

    System.out.println("\nTid quicksort med forbedring, på tabell uten duplikater:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksortMedForbedring(table7WithoutDuplicates1_000_000, 10) + ",     " + test1(table7WithoutDuplicates1_000_000) + " " + test2WithoutDuplicates(table7WithoutDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksortMedForbedring(table8WithoutDuplicates10_000_000, 10) + ",     " + test1(table8WithoutDuplicates10_000_000) + " " + test2WithoutDuplicates(table8WithoutDuplicates10_000_000));



    System.out.println("\n\nTid quicksort uten forbedring, på tabell med mange duplikater, som er sortert fra før:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksort(table1WithDuplicates1_000_000, 10) + ",     " + test1(table1WithDuplicates1_000_000) + " " + test2WithDuplicates(table1WithDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksort(table2WithDuplicates10_000_000, 10) + ",     " + test1(table2WithDuplicates10_000_000) + " " + test2WithDuplicates(table2WithDuplicates10_000_000));

    System.out.println("\nTid quicksort med forbedring, på tabell med mange duplikater, som er sortert fra før:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksortMedForbedring(table3WithDuplicates1_000_000, 10) + ",     " + test1(table3WithDuplicates1_000_000) + " " + test2WithDuplicates(table3WithDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksortMedForbedring(table4WithDuplicates10_000_000, 10) + ",     " + test1(table4WithDuplicates10_000_000) + " " + test2WithDuplicates(table4WithDuplicates10_000_000));

    System.out.println("\nTid quicksort uten forbedring, på tabell uten duplikater, som er sortert fra før:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksort(table5WithoutDuplicates1_000_000, 10) + ",     " + test1(table5WithoutDuplicates1_000_000) + " " + test2WithoutDuplicates(table5WithoutDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksort(table6WithoutDuplicates10_000_000, 10) + ",     " + test1(table6WithoutDuplicates10_000_000) + " " + test2WithoutDuplicates(table6WithoutDuplicates10_000_000));

    System.out.println("\nTid quicksort med forbedring, på tabell uten duplikater, som er sortert fra før:");
    System.out.println("Størrelse = 1 000 000: " + getTimeQuicksortMedForbedring(table7WithoutDuplicates1_000_000, 10) + ",     " + test1(table7WithoutDuplicates1_000_000) + " " + test2WithoutDuplicates(table7WithoutDuplicates1_000_000));
    System.out.println("Størrelse = 10 000 000: " + getTimeQuicksortMedForbedring(table8WithoutDuplicates10_000_000, 10) + ",     " + test1(table8WithoutDuplicates10_000_000) + " " + test2WithoutDuplicates(table8WithoutDuplicates10_000_000));
  }

  private static void quicksortMedForbedringMaxMin(int[] t) {
    int minIndex = findMinIndex(t);
    int maxIndex = findMaxIndex(t);
    bytt(t, 0, minIndex);
    bytt(t, t.length - 1, maxIndex);
    quicksortMedForbedring(t, 1, t.length - 2);
  }

  private static void quicksortMedForbedring(int[] t, int v, int h) {
    if (h - v > 2) {
      if (t[v - 1] == t[h + 1]) return;
      int delepos = splitt(t, v, h);
      quicksortMedForbedring(t, v, delepos - 1);
      quicksortMedForbedring(t, delepos + 1, h);
    } else median3sort(t, v, h);
  }

  private static void quicksort(int[] t, int v, int h) {
    if (h - v > 2) {
      int delepos = splitt(t, v, h);
      quicksort(t, v, delepos - 1);
      quicksort(t, delepos + 1, h);
    } else median3sort(t, v, h);
  }

  private static int findMinIndex(int[] t) {
    int minIndex = 0;
    int min = t[0];

    for (int i = 0; i < t.length; i++) {
      if (t[i] < min) {
        minIndex = i;
        min = t[i];
      }
    }
    return minIndex;
  }

  private static int findMaxIndex(int[] t) {
    int maxIndex = 0;
    int max = t[0];

    for (int i = 0; i < t.length; i++) {
      if (t[i] > max) {
        maxIndex = i;
        max = t[i];
      }
    }
    return maxIndex;
  }

  private static int median3sort(int []t, int v, int h) {
    int m = (v + h) / 2;
    if (t[v] > t[m]) bytt(t, v, m);
    if (t[m] > t[h]) {
      bytt(t, m, h);
      if (t[v] > t[m]) bytt(t, v, m);
    }
    return m;
  }

  private static int splitt(int []t, int v, int h) {
    int iv, ih;
    int m = median3sort(t, v, h);
    int dv = t[m];
    bytt(t, m, h - 1);
    for (iv = v, ih = h - 1;;) {
      while (t[++iv] < dv) ;
      while (t[--ih] > dv) ;
      if (iv >= ih) break;
      bytt(t, iv, ih);
    }
    bytt(t, iv, h-1);
    return iv;
  }

  private static void bytt(int[]t, int v, int h) {
    int temp = t[v];
    t[v] = t[h];
    t[h] = temp;
  }

  private static int[] generateDataWithoutDuplicates (int size) {
    int[] data = new int[size];
    for (int i = 0; i < size; i++) {
      data[i] = i;
    }
    shuffleArray(data);
    return data;
  }

  private static int[] generateDataWithDuplicates (int size) {
    int[] data = new int[size];
    for (int i = 0; i < size; i += 2) {
      data[i] = i / 2;
      data[i + 1] = 42;
    }
    shuffleArray(data);
    return data;
  }

  public static void printTable(int[] t) {
    String s = "";
    for (int i = 0; i < t.length; i++) {
      s += t[i] + ", ";
    }
    System.out.println(s);
  }

  private static void shuffleArray(int[] array) {
    Random rnd = new Random();
    for (int i = array.length - 1; i > 0; i--) {
      int index = rnd.nextInt(i + 1);
      // Simple swap
      int a = array[index];
      array[index] = array[i];
      array[i] = a;
    }
  }

  private static String test1 (int[] t) {
    for (int i = 0; i < t.length - 1; i++) {
      if (t[i] <= t[i + 1]) {
        return "Test 1 godkjent.";
      }
    }
    return "Test 1 feilet. Tallene er ikke sortert riktig.";
  }

  private static String test2WithoutDuplicates (int[] t) {
    if (Arrays.stream(t).sum() == Arrays.stream(generateDataWithoutDuplicates(t.length)).sum()) {
      return "Test 2 godkjent.";
    }
    return "Test 2 feilet. Et eller flere tall har blitt overskrevet.";
  }

  private static String test2WithDuplicates (int[] t) {
    if (Arrays.stream(t).sum() == Arrays.stream(generateDataWithDuplicates(t.length)).sum()) {
      return "Test 2 godkjent.";
    }
    return "Test 2 feilet. Et eller flere tall har blitt overskrevet.";
  }

  private static String getTimeQuicksort(int[] t, int numRuns) {
    long totalTime = 0;

    for (int i = 0; i < numRuns; i++) {
      int[] copy = t.clone();  // Create a copy of the array for each run
      long start = System.currentTimeMillis();
      quicksort(copy, 0, copy.length - 1);
      long end = System.currentTimeMillis();
      totalTime += (end - start);
    }

    return "Average elapsed time in milli seconds over " + numRuns + " runs: " + (totalTime / numRuns);
  }

  private static String getTimeQuicksortMedForbedring(int[] t, int numRuns) {
    long totalTime = 0;

    for (int i = 0; i < numRuns; i++) {
      int[] copy = t.clone();  // Create a copy of the array for each run
      long start = System.currentTimeMillis();
      quicksortMedForbedringMaxMin(copy);
      long end = System.currentTimeMillis();
      totalTime += (end - start);
    }

    return "Average elapsed time in milli seconds over " + numRuns + " runs: " + (totalTime / numRuns);
  }
}