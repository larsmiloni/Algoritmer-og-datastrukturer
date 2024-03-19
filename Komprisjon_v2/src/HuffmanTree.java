import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class HuffmanNode implements Comparable<HuffmanNode> {
  char ch;
  int frequency;
  HuffmanNode left = null, right = null;

  HuffmanNode(char ch, int frequency) {
    this.ch = ch;
    this.frequency = frequency;
  }

  HuffmanNode(HuffmanNode left, HuffmanNode right) {
    this.frequency = left.frequency + right.frequency;
    this.left = left;
    this.right = right;
  }

  @Override
  public int compareTo(HuffmanNode node) {
    return this.frequency - node.frequency;
  }
}

public class HuffmanTree {
  private HuffmanNode root;
  private Map<Character, String> charToCodeMap = new HashMap<>();
  private Map<String, Character> codeToCharMap = new HashMap<>();

  public HuffmanTree(String text) {
    if (text == null || text.isEmpty()) {
      throw new IllegalArgumentException("Cannot encode null or empty text.");
    }

    // Count frequency of each character.
    Map<Character, Integer> freq = new HashMap<>();
    for (char c : text.toCharArray()) {
      freq.put(c, freq.getOrDefault(c, 0) + 1);
    }

    // Create priority queue.
    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
    for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
      pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
    }

    // Build Huffman Tree
    while (pq.size() > 1) {
      HuffmanNode left = pq.poll();
      HuffmanNode right = pq.poll();
      HuffmanNode parent = new HuffmanNode(left, right);
      pq.add(parent);
    }

    // Root of the tree
    root = pq.poll();
    // Build codes
    buildCodes(root, "");
  }

  // Getter for the character-to-code map
  public Map<Character, String> getCharToCodeMap() {
    return charToCodeMap;
  }

  // Getter for the code-to-character map
  public Map<String, Character> getCodeToCharMap() {
    return codeToCharMap;
  }

  private void buildCodes(HuffmanNode node, String code) {
    if (node != null) {
      if (node.left == null && node.right == null) {
        charToCodeMap.put(node.ch, code);
        codeToCharMap.put(code, node.ch);
      } else {
        buildCodes(node.left, code + "0");
        buildCodes(node.right, code + "1");
      }
    }
  }
}
