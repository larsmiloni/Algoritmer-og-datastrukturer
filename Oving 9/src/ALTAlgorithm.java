import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ALTAlgorithm {
  private Map<Integer, Map<Integer, Integer>> graph; // Graf representert som en adjacency liste
  private Map<Integer, Integer>[] landmarkDistances; // Avstander fra hvert landemerke til hver node

  public ALTAlgorithm(Map<Integer, Map<Integer, Integer>> graph, Map<Integer, Integer>[] landmarkDistances) {
    this.graph = graph;
    this.landmarkDistances = landmarkDistances;
  }

  // A* søk med Landmark-basert heuristikk
  public void aStarSearch(int startNode, int goalNode) {
    int n = graph.size();
    int[] distances = new int[n]; // Avstander fra startNode til hver node
    Arrays.fill(distances, Integer.MAX_VALUE);
    distances[startNode] = 0;

    PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(node -> node.cost + heuristic(node.node, goalNode)));
    queue.add(new Node(startNode, 0));

    while (!queue.isEmpty()) {
      Node current = queue.poll();

      if (current.node == goalNode) {
        printPath(distances, goalNode);
        return;
      }

      Map<Integer, Integer> neighbors = graph.getOrDefault(current.node, new HashMap<>());
      for (Map.Entry<Integer, Integer> neighbor : neighbors.entrySet()) {
        int neighborNode = neighbor.getKey();
        int edgeWeight = neighbor.getValue();

        int newDist = distances[current.node] + edgeWeight;
        if (newDist < distances[neighborNode]) {
          distances[neighborNode] = newDist;
          queue.add(new Node(neighborNode, newDist));
        }
      }
    }

    System.out.println("Path not found");
  }

  // Landmark-basert heuristikk
  private int heuristic(int node, int goal) {
    int h = 0;
    for (Map.Entry<Integer, Integer> entry : landmarkDistances[node].entrySet()) {
      int landmark = entry.getKey();
      int distToLandmark = entry.getValue();
      int landmarkToGoal = landmarkDistances[goal].getOrDefault(landmark, Integer.MAX_VALUE);
      h = Math.max(h, Math.abs(distToLandmark - landmarkToGoal));
    }
    return h;
  }

  // Hjelpeklasse for å representere en node i prioritetskøen
  private static class Node {
    int node;
    int cost;

    Node(int node, int cost) {
      this.node = node;
      this.cost = cost;
    }
  }

  // Funksjon for å skrive ut den korteste stien
  private void printPath(int[] distances, int goalNode) {
    System.out.println("Shortest path length to " + goalNode + ": " + distances[goalNode]);
  }

  private static Map<Integer, Integer>[] loadLandmarkDistances(int[] landmarks) throws FileNotFoundException {
    Map<Integer, Integer>[] landmarkDistances = new HashMap[landmarks.length];
    for (int i = 0; i < landmarks.length; i++) {
      int landmark = landmarks[i];
      File file = new File("distances_from_landmark_" + landmark + ".txt");
      Scanner scanner = new Scanner(file);
      landmarkDistances[i] = new HashMap<>();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] parts = line.split(": ");
        int node = Integer.parseInt(parts[0].split(" ")[1]);
        int distance = Integer.parseInt(parts[1]);
        System.out.println("Node: " + node + "Distance: " + distance);
        landmarkDistances[i].put(node, distance);
      }
      scanner.close();
    }
    return landmarkDistances;
  }

  public static void main(String[] args) throws FileNotFoundException {
    // Anta at du har en liste med landemerker
    int[] landmarks = { 16, 416 };

    Map<Integer, Map<Integer, Integer>> graph = DijkstraAlgorithm.graph("island-kanter.txt");
    Map<Integer, Integer>[] landmarkDistances = loadLandmarkDistances(landmarks);

    ALTAlgorithm alt = new ALTAlgorithm(graph, landmarkDistances);
    alt.aStarSearch(0, 0); // Endre disse for å teste forskjellige ruter
  }
}
