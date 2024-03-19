import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DijkstraAlgorithm {
  private static final int NO_PARENT = -1;

  // Modifisert til å returnere korteste avstands-array
  public static Result dijkstra(Map<Integer, Map<Integer, Integer>> graph, int startVertex, int endVertex, String vertexFilePath) throws FileNotFoundException {
    File fileNode = new File(vertexFilePath);
    Scanner scannerMaxNode = new Scanner(fileNode);
    int antallNoder = scannerMaxNode.nextInt();
    scannerMaxNode.close();

    int[] shortestDistances = new int[antallNoder];
    boolean[] added = new boolean[antallNoder];
    int[] parents = new int[antallNoder];  // Array to hold the shortest path tree
    for (int vertexIndex = 0; vertexIndex < antallNoder; vertexIndex++) {
      parents[vertexIndex] = NO_PARENT;
    }

    for (int vertexIndex = 0; vertexIndex < antallNoder; vertexIndex++) {
      shortestDistances[vertexIndex] = Integer.MAX_VALUE;
      added[vertexIndex] = false;
      parents[vertexIndex] = NO_PARENT;
    }

    shortestDistances[startVertex] = 0;

    // Use a priority queue to store vertices with their distances
    PriorityQueue<Map.Entry<Integer, Integer>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));
    priorityQueue.add(new AbstractMap.SimpleEntry<>(startVertex, 0));

    while (!priorityQueue.isEmpty()) {
      int nearestVertex = priorityQueue.poll().getKey();
      added[nearestVertex] = true;

      Map<Integer, Integer> adjacentVertices = graph.getOrDefault(nearestVertex, new HashMap<>());
      for (Map.Entry<Integer, Integer> entry : adjacentVertices.entrySet()) {
        int vertexIndex = entry.getKey();
        int edgeDistance = entry.getValue();

        if (!added[vertexIndex] && (shortestDistances[nearestVertex] + edgeDistance) < shortestDistances[vertexIndex]) {
          parents[vertexIndex] = nearestVertex;
          shortestDistances[vertexIndex] = shortestDistances[nearestVertex] + edgeDistance;
          priorityQueue.add(new AbstractMap.SimpleEntry<>(vertexIndex, shortestDistances[vertexIndex]));
        }
      }
    }

    List<Integer> path = new ArrayList<>();
    int currentNode = endVertex;
    while (currentNode != NO_PARENT) {
      path.add(currentNode);
      currentNode = parents[currentNode];
    }
    Collections.reverse(path);

    return new Result(shortestDistances, path);
  }

  // Inner class to hold both the distances array and the path
  public static class Result {
    public final int[] distances;
    public final List<Integer> path;

    public Result(int[] distances, List<Integer> path) {
      this.distances = distances;
      this.path = path;
    }
  }

  public static Map<Integer, Map<Integer, Integer>> graph(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    Scanner scanner = new Scanner(file);
    scanner.nextLine(); // Hopp over den første linjen

    Map<Integer, Map<Integer, Integer>> graph = new HashMap<>();

    while (scanner.hasNextLine()) {
      int fromNode = scanner.nextInt();
      int toNode = scanner.nextInt();
      int driveTime = scanner.nextInt();
      scanner.nextLine(); // Hopp over resten av linjen

      graph.putIfAbsent(fromNode, new HashMap<>());
      graph.get(fromNode).put(toNode, driveTime);

      // Hvis grafen er urettet
      graph.putIfAbsent(toNode, new HashMap<>());
      graph.get(toNode).put(fromNode, driveTime);
    }
    scanner.close();

    return graph;
  }

  public static String findShortestPath(int startVertex, int endVertex, String filePath) throws FileNotFoundException {
    // Perform Dijkstra's algorithm from the start vertex
    Result result = dijkstra(graph(filePath), startVertex, endVertex, filePath);

    // Check if a path exists to the end vertex
    if (result.distances[endVertex] == Integer.MAX_VALUE) {
      return "No path exists from " + startVertex + " to " + endVertex;
    }

    // Build the string representation of the path
    StringBuilder pathString = new StringBuilder();
    for (int node : result.path) {
      pathString.append(node).append(" -> ");
    }
    pathString.delete(pathString.length() - 4, pathString.length()); // Remove the last " -> "

    // Return the distance and the path as a string
    return "Shortest distance from " + startVertex + " to " + endVertex + " is " + result.distances[endVertex] + "\nPath: " + pathString;
  }


  // Driver Code tilpasset for preprosessering
  public static void main(String[] args) throws FileNotFoundException {
    //finne korteste vei fra en node til en annen.
    System.out.println(findShortestPath(565, 38399, "norden-kanter.txt"));
  }
}