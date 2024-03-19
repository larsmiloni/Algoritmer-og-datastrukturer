import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
  public static void main(String[] args) throws IOException {
    /**
    String test = "this is a car";
    HuffmanTree tree = new HuffmanTree(test);
    Map<Character, String> charToCode = tree.getCharToCodeMap();
    Map<String, Character> codeToChar = tree.getCodeToCharMap();
    System.out.println("Character to Code Map: " + charToCode);
    System.out.println("Code to Character Map: " + codeToChar);
     **/
    try {
      LZ77Huffman lh = new LZ77Huffman("diverse.lyx");

      List<Token> encodedTokens = lh.encode("diverse.lyx");

      for (Token token : encodedTokens) {
        System.out.print(token);
      }

      lh.writeToBinaryFile(encodedTokens, "diverse-ut.lyx");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}