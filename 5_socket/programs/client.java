import java.util.*;
import java.io.*;
import java.net.*;

public class client {
  public static String cin()throws IOException{return (new BufferedReader(new InputStreamReader(System.in))).readLine();}
  public static void cout(String str){System.out.print(str);}
  public static void main(String[]args)throws IOException{
    String ip = "192.168.3.33";
    int port = 1145;
    Socket socket = new Socket(ip,port);
    while(true){
      BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String query = reader.readLine();
      cout(query);
      PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
      String line = cin();
      cout("post : "+line+"\n");
      writer.println(line);
      writer.flush();
      if(line.equals("end"))break;
    }
    socket.close();
  }
}
