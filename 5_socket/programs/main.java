import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class main {
  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    PrintWriter writer = new PrintWriter(System.out);
    String line = new String();
    while ((line = reader.readLine()) != null) {
      System.out.println("system out: " + line);
      writer.println("print writer: " + line);
    }
    writer.flush();
  }
}
