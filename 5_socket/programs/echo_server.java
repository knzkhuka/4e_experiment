import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class echo_server {
  public static void main(String[] args) {
    int port = 10101;
    ServerSocket server = null;
    try {
      server = new ServerSocket(port);
      System.out.println("start server on port " + server.getLocalPort());
      while (true) {
        Socket socket = server.accept();
        new echo_thread(socket).start();
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

class echo_thread extends Thread {
  private Socket socket;

  echo_thread(Socket socket) {
    this.socket = socket;
    System.out.println("connected : " + socket.getRemoteSocketAddress());
  }

  public void run() {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      PrintWriter writer = new PrintWriter(socket.getOutputStream());
      String line = new String();
      while ((line = reader.readLine()) != null) {
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