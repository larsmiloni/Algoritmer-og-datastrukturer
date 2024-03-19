import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LZ77Huffman {
  private static final int MAX_WINDOW_SIZE = 4096;
  private HuffmanTree huffmanTree;

  public LZ77Huffman(String filePath) throws IOException {
    String fileContent = readFileAsString(filePath);
    this.huffmanTree = new HuffmanTree(fileContent);
  }

  public List<Token> encode(String filePath) throws IOException {
    String input = readFileAsString(filePath);

    List<Token> tokens = new LinkedList<>();
    int pos = 0;

    while (pos < input.length()) {
      int searchStart = Math.max(0, pos - MAX_WINDOW_SIZE);
      String searchBuffer = input.substring(searchStart, pos);
      String lookAheadBuffer = input.substring(pos, Math.min(pos + MAX_WINDOW_SIZE, input.length()));

      int matchLength = 0;
      int matchDistance = -1;

      // Find the longest match
      for (int i = 1; i <= lookAheadBuffer.length(); i++) {
        String currentMatch = lookAheadBuffer.substring(0, i);
        int foundIndex = searchBuffer.lastIndexOf(currentMatch);

        if (foundIndex != -1) {
          matchLength = i;
          matchDistance = pos - (searchStart + foundIndex);
        } else {
          break;
        }
      }

      // Output a token for the match or for a single literal if no match is found
      if (matchDistance == -1) {
        tokens.add(new Token(input.charAt(pos)));
        pos++;
      } else {
        tokens.add(new Token(matchLength, matchDistance));
        pos += matchLength;
      }
    }

    return tokens;
  }

  public String decode(List<Token> tokens) {
    StringBuilder output = new StringBuilder();
    for (Token token : tokens) {
      if (token.isLiteral) {
        output.append(token.literal);
      } else {
        // Note: StringBuilder indexing is 0-based and distance is 1-based
        int startPos = output.length() - token.distance;
        for (int i = 0; i < token.length; i++) {
          output.append(output.charAt(startPos + i));
        }
      }
    }
    return output.toString();
  }

  public void writeToBinaryFile(List<Token> tokens, String filename) throws IOException {
    try (BitOutputStream out = new BitOutputStream(new FileOutputStream(filename))) {
      // Write the length of the Huffman tree codes (necessary for decoding)
      out.writeBits(String.format("%16s", Integer.toBinaryString(huffmanTree.getCharToCodeMap().size())).replace(' ', '0'));

      // Write the Huffman tree codes
      for (Map.Entry<Character, String> entry : huffmanTree.getCharToCodeMap().entrySet()) {
        String charBits = Integer.toBinaryString((int) entry.getKey());
        // Assuming the character set is ASCII, we use 8 bits per character
        out.writeBits(String.format("%8s", charBits).replace(' ', '0'));
        // The length of the Huffman code for this character
        out.writeBits(String.format("%8s", Integer.toBinaryString(entry.getValue().length())).replace(' ', '0'));
        // The Huffman code itself
        out.writeBits(entry.getValue());
      }

      // Write the actual encoded data
      for (Token token : tokens) {
        if (token.isLiteral) {
          // Write flag bit for a literal
          out.writeBit(0);
          String bits = huffmanTree.getCharToCodeMap().get(token.literal);
          out.writeBits(bits);
        } else {
          // Write flag bit for a length/distance pair
          out.writeBit(1);

          // Write the distance, padded to 12 bits
          String distanceBits = String.format("%12s", Integer.toBinaryString(token.distance)).replace(' ', '0');
          out.writeBits(distanceBits);

          // Write the length, padded to 8 bits
          String lengthBits = String.format("%8s", Integer.toBinaryString(token.length)).replace(' ', '0');
          out.writeBits(lengthBits);
        }
      }
    }
  }

  class BitOutputStream implements AutoCloseable {
    private OutputStream output;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(OutputStream out) {
      output = out;
      currentByte = 0;
      numBitsFilled = 0;
    }

    public void writeBit(int bit) throws IOException {
      if (!(bit == 0 || bit == 1))
        throw new IllegalArgumentException("Argument must be 0 or 1");
      currentByte = (currentByte << 1) | bit;
      numBitsFilled++;
      if (numBitsFilled == 8) {
        output.write(currentByte);
        numBitsFilled = 0;
        currentByte = 0;
      }
    }

    public void writeBits(String bits) throws IOException {
      for (char bit : bits.toCharArray())
        writeBit(bit - '0'); // Assumes bit is '0' or '1'.
    }

    @Override
    public void close() throws IOException {
      while (numBitsFilled != 0)
        writeBit(0);
      output.close();
    }
  }

  private static String readFileAsString(String filePath) throws IOException {
    return new String(Files.readAllBytes(Paths.get(filePath)));
  }

  public static void main(String[] args) {
    /**
    String test = "This is a test. This test is only a test.";
    List<Token> encoded = encode(test);
    System.out.println("Encoded:");
    for (Token token : encoded) {
      System.out.print(token);
    }
    System.out.println("\n\nDecoded:");
    String decoded = decode(encoded);
    System.out.println(decoded);
     **/
  }
}
