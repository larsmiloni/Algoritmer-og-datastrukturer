import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ShortestPath {
  public static final String nodesFP = "norden-noder.txt";
  public static final String edgesFP = "norden-kanter.txt";
  public static final String placesFP = "norden-interessepkt.txt";
  public static final String toALT_TABLE = "to_alt_tables_Norden.txt";
  public static final String fromALT_TABLE = "from_alt_tables_Norden.txt";
  private static final int numberOfLandmarks = 5;

  public static void main(String[] args) throws IOException {
    int orkanger = 2948202;
    int trondheim = 7826348;
    int selbustrand = 5009309;
    Graph initialize = new Graph();
    int greenstarhotellahti = 999080;
    Node[] nodes = initialize.readNodes(nodesFP);
    initialize.readWEdges(edgesFP);
    testDijkstra(orkanger, trondheim, nodes, false);
    testALT(orkanger, trondheim, nodes, false);

    findNearestPlaces(2266026, Place.CHARGING_STATION, 5, nodes);
  }
  /**
   * Returns node array so that we don't have to read from file twice
   */
  private static void testDijkstra(int startIdx, int endIdx, Node[] nodes, boolean route) throws IOException {
    Graph dijkstraGraph = new Graph();
    if (nodes == null) {
      dijkstraGraph.readNodes(ShortestPath.nodesFP);
      dijkstraGraph.readWEdges(ShortestPath.edgesFP);
    } {
      dijkstraGraph.node = nodes;
      dijkstraGraph.N = nodes.length;
      dijkstraGraph.reset(nodes);
    }
    dijkstraGraph.readPlaces(placesFP);
    System.out.println("------Finding route from node " + startIdx + " to node " +  endIdx + " with Dijkstra: ------");
    long startTime = System.currentTimeMillis();
    Node endDijkstra = dijkstraGraph.dijkstra(startIdx, endIdx, nodes);
    long endTime = System.currentTimeMillis();
    double duration = ((double) endTime - startTime) / 1000;
    System.out.println("Runtime Dijkstra: " + duration + " seconds");
    String travelTime = secToTime(endDijkstra.prior.dist / 100);
    System.out.println("Travel time: " + travelTime);
    if (route) printRoute(endIdx, nodes);
  }

  private static void testALT(int startIdx, int endIdx, Node[] nodes, boolean route) throws IOException {
    Graph ALTGraph = new Graph();
    if (nodes == null) {
      ALTGraph.readNodes(ShortestPath.nodesFP);
      ALTGraph.readWEdges(ShortestPath.edgesFP);
    } {
      ALTGraph.node = nodes;
      ALTGraph.N = nodes.length;
      ALTGraph.reset(nodes);
    }
    ALTGraph.distanceFromLandmarks = ALTGraph.readALTTable(ShortestPath.fromALT_TABLE, ALTGraph.N, ShortestPath.numberOfLandmarks);
    ALTGraph.distanceToLandmarks = ALTGraph.readALTTable(ShortestPath.toALT_TABLE, ALTGraph.N, ShortestPath.numberOfLandmarks);
    System.out.println("------Finding route from node " + startIdx + " to node " +  endIdx + " with ALT: ------");
    long startTime = System.currentTimeMillis();
    Node endALT = ALTGraph.ALT(startIdx, endIdx, nodes);
    long endTime = System.currentTimeMillis();
    double duration = ((double) endTime - startTime) / 1000;
    System.out.println("Runtime ALT: " + duration + " seconds");
    String travelTime = secToTime(endALT.prior.dist / 100);
    System.out.println("Travel time: " + travelTime);
    if (route) printRoute(endIdx, nodes);

  }

  private static void printRoute(int endIdx, Node[] nodes) {

    Prior prior = nodes[endIdx].prior;
    LinkedList<String> route = new LinkedList<>();
    while (prior.prevNode != null) {
      route.addFirst(prior.prevNode.coordinateString());
      prior = prior.prevNode.prior;
    }
    System.out.println("---Route---");
    int count = 0;
    for (String coordinate : route) {
      //if (count % 2 == 0)
        System.out.println(coordinate);
      count++;
    }
  }

  private static void findNearestPlaces(int startIdx, int type, int limit, Node[] nodes) throws IOException {
    Graph dijkstraGraph = new Graph();
    if (nodes == null) dijkstraGraph.readNodes(nodesFP);
    else {
      dijkstraGraph.node = nodes;
      dijkstraGraph.N = nodes.length;
    }
    dijkstraGraph.reset(nodes);
    dijkstraGraph.readPlaces(placesFP);
    System.out.println("----------------Places------------------");
    Node[] closestPoints = dijkstraGraph.dijkstraFindType(startIdx, type, limit);
    Arrays.stream(closestPoints).forEach(node -> {
      String name = dijkstraGraph.places.get(node.id).name;
      System.out.println(name.substring(1, name.length() - 1));
    });
    System.out.println("----------------Coordinates------------------");
    Arrays.stream(closestPoints).forEach(node -> System.out.println(node.coordinateString()));
  }

  private static String secToTime(int sec){
    int hours = sec / 3600;
    int minutes = sec % 3600 / 60;
    int seconds = sec % 3600 % 60;
    return hours + ":" + minutes + ":" + seconds;
  }

  public static class Graph {
    int N, E;
    int[][] distanceTable, distanceToLandmarks, distanceFromLandmarks;
    Node[] node, landmarks;
    private PriorityQueue<Node> prioQ;
    Map<Integer, Place> places = new HashMap<>();

    public void readPlaces(String path) throws IOException {
      File file = new File(path);
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line = br.readLine(); // skip first
        while ((line = br.readLine()) != null) {
          String[] parts = line.split("\\s+", 3);
          int nodeNumber = Integer.parseInt(parts[0]);
          int code = Integer.parseInt(parts[1]);
          String name = parts[2];
          if (places.containsKey(nodeNumber)) {
            places.get(nodeNumber).updateCode(code);
            continue;
          }
          Place place = new Place(nodeNumber, code, name);
          places.put(nodeNumber, place);
        }
      }
    }

    public Node ALT(int startIdx, int endIdx, Node[] nodes) {
      Node startNode = nodes[startIdx];
      if (startNode == null) return null;
      startNode.prior.dist = 0;

      if (endIdx == startNode.id) return nodes[endIdx];

      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.getFullDistance())));

      prioQ.add(startNode);

      int visited = 1;
      while (!prioQ.isEmpty()) {
        Node n = prioQ.poll();

        visited++;

        n.found = true;
        if (n.id == endIdx) {
          System.out.println("ALT visited " + visited + " nodes");
          return n;
        }

        for (WEdge e = n.kant1; e != null; e = e.next) {
          shorten(n, e, endIdx, nodes);
        }
      }
      return null;
    }

    public Node dijkstra(int startIdx, int endIdx, Node[] nodes) {
      Node startNode = nodes[startIdx];
      if (startNode == null) return null;
      startNode.prior.dist = 0;

      if (endIdx == startNode.id) return nodes[endIdx];

      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.dist)));
      prioQ.add(startNode);

      int visited = 1;
      while (!prioQ.isEmpty()) {
        Node n = prioQ.poll();
        visited++;
        n.found = true;
        if (n.id == endIdx) {
          System.out.println("Dijkstra visited " + visited + " nodes");
          return n;
        }

        for (WEdge e = n.kant1; e != null; e = e.next) {
          shorten(n, e);
        }
      }
      return null;
    }

    /**
     * Finner maks node distanse
     * @param index
     * @return
     */
    public Node[] getLandMarks(int index, int limit) {

      distanceTable = new int[node.length][limit]; //Table which will be written to file

      Node startNode = node[index];
      landmarks = new Node[limit];

      landmarks[0] = findFurthestNode(startNode); //Find first landmark
      System.out.println("L1: " + landmarks[0].id);

      for (int i = 0; i < limit-1; i++) {
        Node landmark;

        reset(node);

        if (i == 0) { //Finding second landmark - when we only have found one landmark there we dont need to consider summing distances of two or morelandmarks
          landmark = findFurthestNode(landmarks[i], distanceTable, i);
        }
        else { //Finding third+ landmark
          landmark = findFurthestNodeV2(landmarks[i], distanceTable, i);
        }
        landmarks[i+1] = landmark;
        System.out.println("L" + (i+2) + ": " + landmark.id);
      }
      return landmarks;
    }


    /**
     * Returns distance between a given landmark and a node.
     * @return
     */
    public int getLandMarkSum(Node node, int nrLandmarks) {
      int sum = 0;
      for (int i = 0; i < nrLandmarks; i++) {
        sum += distanceTable[node.id][i];
      }
      return sum;
    }

    void reset(Node[] nodes) {
      if (nodes != null) {
        for (Node node : nodes) {
          node.found = false;
          node.prior = new Prior();
        }
      }
    }

    public Node findFurthestNode(Node node) {
      Node n = null;
      node.prior.dist = 0;
      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.dist)));
      prioQ.add(node);

      while (!prioQ.isEmpty()) {
        n = prioQ.poll();
        n.found = true;

        for (WEdge e = n.kant1; e != null; e = e.next) {
          shortenLength(n, e);
        }
      }
      return n;
    }

    /**
     * Finds node that is furthest from reference landmark
     * @param node reference node
     * @param distances
     * @param landmarkIdx index of landmark in landmark array that we are currently
     *                    finding distances to all other nodes
     * @return
     */
    public Node findFurthestNode(Node node, int[][] distances, int landmarkIdx) {
      Node n = null;
      node.prior.dist = 0;
      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.dist)));
      prioQ.add(node);

      while (!prioQ.isEmpty()) {
        n = prioQ.poll();
        n.found = true;

        for (WEdge e = n.kant1; e != null; e = e.next) {
          shortenLength(n, e);
        }
        distances[n.id][landmarkIdx] = n.prior.dist;
      }
      return n;
    }

    /**
     * Finds the node that is furthest from several nodes.
     * Note to self: the current landmark you are on (node) has yet
     * to fill its columns up in the table - these are the distances you are finding now
     * @param node Reference node - can be indexed by the landmarkIdx in the landmarks array
     * @param landmarkIdx index of landmark in landmark array that we are currently
     *                    finding distances to all other nodes
     * @return
     */
    public Node findFurthestNodeV2(Node node, int[][] distances, int landmarkIdx) {
      node.prior.dist = 0;
      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.dist)));
      prioQ.add(node);

      int max = 0;
      Node n;
      Node maxNode = null;
      while (!prioQ.isEmpty()) {
        n = prioQ.poll();

        //Sum of distances from landmarks to n + distance from startnode to n
        int landmarkSum = getLandMarkSum(n, landmarkIdx) + n.prior.dist;

        if (landmarkSum > max) {
          max = landmarkSum;
          maxNode = n;
        }

        n.found = true;
        for (WEdge e = n.kant1; e != null; e = e.next) {
          shortenLength(n, e);
        }
        distances[n.id][landmarkIdx] = n.prior.dist;
      }
      return maxNode;
    }

    public void shorten(Node n, WEdge k, int endIdx, Node[] nodes){
      if (k.to.found) {
        return; // If the node has been found, do not update it
      }
      Prior nd = n.prior, md = k.to.prior;
      if(md.dist>nd.dist + k.travelTime){
        md.dist = nd.dist + k.travelTime;
        md.prevNode = n;
      }

      int estimatedDistance = estimatedDistance(k.to.id, endIdx, landmarks);
      k.to.prior.setHeuristic(estimatedDistance);
      updatePriority(k.to);
    }


    public void shortenLength(Node n, WEdge k) {
      if (k.to.found) {
        return; // If the node has been found, do not update it
      }
      Prior nd = n.prior, md = k.to.prior;
      if(md.dist>nd.dist + k.length){
        md.dist = nd.dist + k.length;
        md.prevNode = n;
      }
      updatePriority(k.to);
    }

    public void shorten(Node n, WEdge e){
      if (e.to.found) {
        return; // If the node has been found, do not update it
      }
      Prior nd = n.prior, md = e.to.prior;
      if(md.dist>nd.dist + e.travelTime){
        md.dist = nd.dist +e.travelTime;
        md.prevNode = n;
      }
      updatePriority(e.to);
    }

    void updatePriority(Node n) {
      prioQ.remove(n);
      prioQ.add(n);
    }


    public Node[] dijkstraFindType(int startNodeIndex, int type, int size) {
      Node[] nodes = new Node[size];
      Node startNode = node[startNodeIndex];
      startNode.prior.dist = 0;
      this.prioQ = new PriorityQueue<>(Comparator.comparingInt(a -> (a.prior.dist)));
      prioQ.add(startNode);
      int count = 0;

      while(!prioQ.isEmpty()) {
        Node n = prioQ.poll();
        n.found = true;

        if (places.get(n.id) != null) {
          if (((places.get(n.id).code & type) == type)) {
            nodes[count] = n;
            count++;
          }
        }

        if (count == size) return nodes;
        for (WEdge e = n.kant1; e != null; e = e.next) {
          shorten(n, e);
        }
      }
      return nodes;
    }

    public int estimatedDistance(int startNodeIdx, int endNodeIdx, Node[] landmarks){
      int maxDistance;
      int superMaxDistance = Integer.MIN_VALUE;
      for (int i = 0; i < landmarks.length; i++) {
        int estimatedDistance1 =distanceToLandmarks[startNodeIdx][i]- distanceToLandmarks[endNodeIdx][i];
        int estimatedDistance2 =distanceFromLandmarks[endNodeIdx][i] - distanceFromLandmarks[startNodeIdx][i];
        if (superMaxDistance < estimatedDistance1 || superMaxDistance < estimatedDistance2) {
          maxDistance = Math.max(estimatedDistance1, estimatedDistance2);
          superMaxDistance = maxDistance;
        }
      }
      return superMaxDistance;
    }


    public Node[] readNodes(String filePath) throws IOException {
      File file = new File(filePath);
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringTokenizer st = new StringTokenizer(reader.readLine());
        this.N = Integer.parseInt(st.nextToken());
        this.node = new Node[N];
        for (int i = 0; i < N; ++i) {
          st = new StringTokenizer(reader.readLine());
          int index = Integer.parseInt(st.nextToken());
          double lat = Double.parseDouble(st.nextToken());
          double lon = Double.parseDouble(st.nextToken());
          node[index] = new Node(index, lat, lon);
        }
      }
      return node;
    }

    public int[][] readALTTable(String filePath, int numberOfNodes, int numberOfLandmarks) throws IOException {
      String[] fields = new String[numberOfLandmarks + 1];
      this.landmarks = new Node[numberOfLandmarks];
      int[][] table = new int[numberOfNodes][numberOfLandmarks];


      try (BufferedReader bf = Files.newBufferedReader(Path.of(filePath))) {
        String currentLine = bf.readLine(); // Find the first landmarks
        split(currentLine, numberOfLandmarks, fields);
        for (int i = 0; i < numberOfLandmarks; i++) this.landmarks[i] = node[Integer.parseInt(fields[i])];

        while((currentLine = bf.readLine()) != null) {
          split(currentLine, numberOfLandmarks + 1, fields);

          int currentNode = Integer.parseInt(fields[0]);

          for (int i = 0; i < numberOfLandmarks; i++) table[currentNode][i] = Integer.parseInt(fields[i + 1]);
        }
      }
      return table;
    }

    public void readWEdges(String filePath) throws IOException {
      File file = new File(filePath);
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        StringTokenizer st = new StringTokenizer(reader.readLine());
        this.E = Integer.parseInt(st.nextToken());
        for (int i = 0; i < E; ++i) {
          st = new StringTokenizer(reader.readLine());
          int from = Integer.parseInt(st.nextToken());
          int to = Integer.parseInt(st.nextToken());
          int time = Integer.parseInt(st.nextToken());
          int distance = Integer.parseInt(st.nextToken());
          int speedlimit = Integer.parseInt(st.nextToken());
          WEdge v = new WEdge(node[to], node[from].kant1, time, distance, speedlimit);
          node[from].kant1 = v;
        }
      }
    }

    private void split(String line, int amount, String[] field) {
      int j = 0;
      int length = line.length();
      for (int i = 0; i < amount; ++i) {
        while (line.charAt(j) <= ' ') ++j;
        int start = j;
        while (j < length && line.charAt(j) > ' ') ++j;
        field[i] = line.substring(start, j);
      }
    }
  }


  public static class WEdge {
    Node to;
    WEdge next;
    int travelTime, length, speedLimit;

    public WEdge(Node n, WEdge next, int travelTime, int length, int speedLimit) {
      this.to = n;
      this.next = next;
      this.travelTime = travelTime;
      this.length = length;
      this.speedLimit = speedLimit;
    }
  }

  public static class Prior {
    int dist;
    Node prevNode;
    int heuristic; // Estimate of distance from this node to the end node
    int inf = 1000000000;
    public Prior(){
      dist = inf;
      heuristic = 0;
    }

    void setHeuristic(int val) {
      heuristic = val;
    }
    int getFullDistance() {
      return heuristic + dist;
    }

    @Override
    public String toString() {
      return "{dist: " + dist + "}";
    }

  }

  public static class Place {
    int nodeNumber;
    int code;
    final String name;
    final String placeID;
    public final static byte LOCATION_NAME = 1;
    public final static byte PETROL_STATION = 2;
    public final static byte CHARGING_STATION = 4;
    public final static byte DINING = 8;
    public final static byte DRINKS = 16;
    public final static byte LODGING = 32;

    public Place(int nodeNumber, int code, String name) {
      this.placeID = name + nodeNumber;
      this.nodeNumber = nodeNumber;
      this.code = code;
      this.name = name;
    }

    public void updateCode(int code) {
      this.code = this.code | code;
    }
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Place that)) return false;
      return nodeNumber == that.nodeNumber && code == that.code && name.equals(that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(nodeNumber, code, name);
    }

    @Override
    public String toString() {
      return String.valueOf(nodeNumber);
    }
  }

  public static class Node {

    int id, type;
    WEdge kant1;
    Prior prior; // Additional node data

    boolean found, isGoal;
    double x, y; // Posisjonskoordinater

    public Node(int id, double x, double y) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.found = false;
      this.prior = new Prior();
      this.type = 0;
    }

    public String coordinateString(){
      return x+ ", " +y;
    }
    @Override
    public String toString() {
      return "id:" + id + " - breddegrad: " + x + " - lengdegrad:"+ this.y + " - Prior: " + prior + " - kant1: " + kant1;
    }
  }
}