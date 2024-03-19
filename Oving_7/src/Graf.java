import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Graf {
  int N, K;
  Node[] node;

  public void ny_vgraf(BufferedReader br) throws IOException {
    StringTokenizer st = new StringTokenizer(br.readLine());
    N = Integer.parseInt(st.nextToken());
    node = new Node[N];
    for (int i = 0; i < N; ++i) node[i] = new Node();
    K = Integer.parseInt(st.nextToken());
    for (int i = 0; i < K; ++i) {
      st = new StringTokenizer(br.readLine());
      int fra = Integer.parseInt(st.nextToken());
      int til = Integer.parseInt(st.nextToken());
      int vekt = Integer.parseInt(st.nextToken());
      VKant k = new VKant(node[til], (VKant)node[fra].kant1, vekt);
      node[fra].kant1 = k;
    }
  }

  public void maxFlow(int sourceNum, int sinkNum) {
    Node source = node[sourceNum];
    Node sink = node[sinkNum];

    int maxFlow = 0;
    int pathCount = 0; // Count of augmenting paths
    Node[] parent = new Node[N]; // This array is filled by BFS and used to store path

    System.out.println("Maksimum flyt fra " + Arrays.asList(node).indexOf(source) + " til " + Arrays.asList(node).indexOf(sink) + " med Edmond-Karp");
    System.out.println("Økning : Flytøkende vei");

    while (true) {
      // Use BFS to find an augmenting path
      boolean hasPath = bfs(source, sink, parent);
      if (!hasPath) break;

      // Compute the bottleneck capacity of the path
      int pathFlow = Integer.MAX_VALUE;
      List<Node> path = new ArrayList<>();
      Node curNode = sink;
      while (curNode != source) {
        path.add(curNode);
        Node prevNode = parent[Arrays.asList(node).indexOf(curNode)];
        VKant edge = findEdge(prevNode, curNode);
        pathFlow = Math.min(pathFlow, edge.flyt);
        curNode = prevNode;
      }
      path.add(source);
      Collections.reverse(path);

      // Print the augmenting path and its bottleneck capacity
      System.out.print(pathFlow + " ");
      for (Node n : path) {
        System.out.print(Arrays.asList(node).indexOf(n) + " ");
      }
      System.out.println();

      // Update the residual capacities
      curNode = sink;
      while (curNode != source) {
        Node prevNode = parent[Arrays.asList(node).indexOf(curNode)];
        VKant edge = findEdge(prevNode, curNode);
        edge.flyt -= pathFlow;

        // Add reverse edge with flow
        VKant reverseEdge = findEdge(curNode, prevNode);
        if (reverseEdge == null) {
          reverseEdge = new VKant(prevNode, (VKant)curNode.kant1, 0);
          curNode.kant1 = reverseEdge;
        }
        reverseEdge.flyt += pathFlow;

        curNode = prevNode;
      }

      maxFlow += pathFlow;
      pathCount++;
    }

    System.out.println("Maksimal flyt ble " + maxFlow);
    System.out.println("Her ble det brukt " + pathCount + " flytøkende veier.");
  }

  private boolean bfs(Node source, Node sink, Node[] parent) {
    boolean[] visited = new boolean[N];
    Queue<Node> queue = new LinkedList<>();
    queue.add(source);
    visited[Arrays.asList(node).indexOf(source)] = true;
    parent[Arrays.asList(node).indexOf(source)] = null;

    while (!queue.isEmpty()) {
      Node current = queue.poll();
      VKant edge = current.kant1;
      while (edge != null) {
        if (!visited[Arrays.asList(node).indexOf(edge.til)] && edge.flyt > 0) {
          parent[Arrays.asList(node).indexOf(edge.til)] = current;
          visited[Arrays.asList(node).indexOf(edge.til)] = true;
          queue.add(edge.til);
          if (edge.til == sink) return true;
        }
        edge = edge.neste;
      }
    }
    return false;
  }

  private VKant findEdge(Node from, Node to) {
    VKant edge = from.kant1;
    while (edge != null) {
      if (edge.til == to) return edge;
      edge = edge.neste;
    }
    return null;
  }
}

class VKant {
  VKant neste;
  Node til;
  int vekt;
  int flyt;
  public VKant(Node n, VKant nst, int vkt) {
    til = n;
    neste = nst;
    vekt = vkt;
    flyt = vkt;
  }
}

class Node {
  VKant kant1;
}