import java.util.*;
import java.io.*;
import java.net.*;

public class client {
  public static void main(String[]args)throws IOException{
    String ip = "192.168.3.33";
    int port = 1145;
    Socket socket = new Socket(ip,port);
    while(true){
      BufferedReader socket_reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String query = socket_reader.readLine();
      System.out.print(query);
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      String line = (new BufferedReader(new InputStreamReader(System.in))).readLine();
      writer.println(line);
      writer.flush();
      if(line.equals("end"))break;
    }
    socket.close();
  }
}
