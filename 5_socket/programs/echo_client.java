import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class echo_client {
  public static void main(String[] args) {
    String ip = "192.168.3.33";
    int port = 10101;
    try {
      Socket socket = new Socket(ip, port);
      System.out.println("connected : " + socket.getRemoteSocketAddress());
      BufferedReader sreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter swriter = new PrintWriter(socket.getOutputStream());
      BufferedReader creader = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        String line = creader.readLine();
        swriter.println(line);
        swriter.flush();
        System.out.println(socket.getRemoteSocketAddress() + "    send " + line);
        line = sreader.readLine();
        System.out.println(socket.getRemoteSocketAddress() + " receive " + line);
        if (line.equals("end"))
          break;
      }
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
