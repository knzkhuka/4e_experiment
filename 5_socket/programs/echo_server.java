import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class echo_server {
  public static void main(String[] args) {
    int port = 10101;
    ServerSocket server = null;
    List<String> log = Collections.synchronizedList(new ArrayList<>());
    TreeMap<String, String> map = new TreeMap<>();
    Map<String, String> amp = Collections.synchronizedSortedMap(map);
    try {
      server = new ServerSocket(port);
      System.out.println("start server on port " + server.getLocalPort());
      while (true) {
        Socket socket = server.accept();
        System.out.println("log:");
        for (String str : log)
          System.out.println(str);
        new Thread(new echo_thread(socket, log)).start();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (server != null)
          server.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

class echo_thread implements Runnable {
  private Socket socket;
  private List<String> log;

  echo_thread(Socket socket, List<String> log) {
    this.socket = socket;
    this.log = log;
    System.out.println("connected : " + socket.getRemoteSocketAddress());
  }

  public void run() {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter writer = new PrintWriter(socket.getOutputStream());
      String line = new String();
      while ((line = reader.readLine()) != null) {
        log.add(line);
        System.out.println(socket.getRemoteSocketAddress() + " receive " + line);
        writer.println(line);
        writer.flush();
        System.out.println(socket.getRemoteSocketAddress() + "    send " + line);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (socket != null)
          socket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}