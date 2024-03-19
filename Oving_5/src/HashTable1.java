import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable1 {
  private int size = 0;
  private int tableSize = 170; // Dobbelt så stor som antall navn
  private LinkedList<String>[] table;
  private int collisions = 0;

  public HashTable1() {
    table = new LinkedList[tableSize];
    for (int i = 0; i < tableSize; i++) {
      table[i] = new LinkedList<>();
    }
  }

  public void insert(String s) {
    int index = hash(s);
    if (table[index].isEmpty()) {
      table[index].add(s);
    } else {
      collisions++;
      System.out.println("Kollisjon mellom: " + s + " og " + table[index].get(0));
      table[index].add(s);
    }
    size++;
  }

  public boolean contains(String s) {
    int index = hash(s);
    return table[index].contains(s);
  }

  private int hash(String s) {
    int hash = 0;
    for (int i = 0; i < s.length(); i++) {
      hash += s.charAt(i) * (i + 1);
    }
    return hash % tableSize;
  }

  public double getLoadFactor() {
    return (double) size / tableSize;
  }

  public int getTotalCollisions() {
    return collisions;
  }

  public static List<String> readNamesFromFile(String fileName) {
    List<String> names = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        names.add(line.trim());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return names;
  }

  public static void main(String[] args) {
    HashTable1 hashTable = new HashTable1();
    List<String> names = readNamesFromFile("src/navn.txt");

    for (String name : names) {
      hashTable.insert(name);
    }

    String searchName = "Lars Mikkel Lødeng Nilsen";
    if (hashTable.contains(searchName)) {
      System.out.println(searchName + " er med i faget.");
    } else {
      System.out.println(searchName + " er ikke med i faget.");
    }

    double loadFactor = hashTable.getLoadFactor();
    System.out.println("Lastfaktor: " + loadFactor);

    int totalCollisions = hashTable.getTotalCollisions();
    double collisionsPerPerson = (double) totalCollisions / names.size();
    System.out.println("Antall kollisjoner per person: " + collisionsPerPerson);

  }
}