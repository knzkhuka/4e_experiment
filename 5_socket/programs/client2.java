import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class client2 {
  public static void main(String[] args) throws IOException {
    String ip = "192.168.3.33";
    int port = 1145;
    Socket socket = new Socket(ip, port);
    while (true) {
      BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String query = socket_reader.readLine();
      System.out.print(query);

      BufferedReader console_reader = new BufferedReader(new InputStreamReader(System.in));
      String line = console_reader.readLine();

      PrintWriter socket_writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      socket_writer.println(line);
      socket_writer.flush();

      if (line.equals("end"))
        break;
      if (line.equals("list")) {
        while (!(line = socket_reader.readLine()).equals("endlist")) {
          System.out.println(line);
        }
        socket_writer.println("");
        socket_writer.flush();
      }
    }
    socket.close();
  }
}
