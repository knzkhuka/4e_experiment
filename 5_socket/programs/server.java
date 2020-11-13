import java.util.*;
import java.io.*;
import java.net.*;

public class server {
  public static void cout(String str){System.out.println(str);}
  public static void main(String[]args) throws IOException{
    int port = 1145;
    ServerSocket server = new ServerSocket(port);
    Socket socket = server.accept();
    while(true){
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      writer.println("plaese enter name : ");
      writer.flush();
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String str = reader.readLine();
      System.out.println("name : "+str);
      if(str.equals("end"))break;
    }
    socket.close();
    server.close();
  }
}
